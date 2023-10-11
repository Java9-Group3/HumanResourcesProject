package com.hrmanagement.service;

import com.hrmanagement.dto.request.*;
import com.hrmanagement.dto.response.*;
import com.hrmanagement.exception.AuthManagerException;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.manager.ICompanyManager;
import com.hrmanagement.manager.IUserProfileManager;
import com.hrmanagement.mapper.IAuthMapper;
import com.hrmanagement.rabbitmq.model.ForgotPasswordMailModel;
import com.hrmanagement.rabbitmq.model.ResetPasswordModel;
import com.hrmanagement.rabbitmq.producer.ForgotPasswordProducer;
import com.hrmanagement.rabbitmq.producer.RegisterMailHelloProducer;
import com.hrmanagement.rabbitmq.producer.RegisterMailProducer;
import com.hrmanagement.rabbitmq.producer.ResetPasswordProducer;
import com.hrmanagement.repository.IAuthRepository;
import com.hrmanagement.repository.entity.Auth;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.repository.entity.enums.EStatus;
import com.hrmanagement.utility.CodeGenerator;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService extends ServiceManager<Auth,Long> {
    private final IAuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegisterMailHelloProducer registerMailHelloProducer;
    private final IUserProfileManager userManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ForgotPasswordProducer forgotPasswordProducer;
    private final RegisterMailProducer registerMailProducer;
    private final ResetPasswordProducer resetPasswordProducer;
    private final ICompanyManager companyManager;
    public AuthService(IAuthRepository authRepository, PasswordEncoder passwordEncoder, RegisterMailHelloProducer registerMailHelloProducer,  IUserProfileManager userManager, JwtTokenProvider jwtTokenProvider, ForgotPasswordProducer forgotPasswordProducer, RegisterMailProducer registerMailProducer, ResetPasswordProducer resetPasswordProducer, ICompanyManager companyManager) {
        super(authRepository);
        this.authRepository=authRepository;
        this.passwordEncoder = passwordEncoder;
        this.registerMailHelloProducer = registerMailHelloProducer;
        this.userManager = userManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.forgotPasswordProducer = forgotPasswordProducer;
        this.registerMailProducer = registerMailProducer;
        this.resetPasswordProducer = resetPasswordProducer;
        this.companyManager = companyManager;
    }


    public Boolean registerVisitor(RegisterVisitorRequestDto dto){
        Optional<Auth> optionalAuth = authRepository.findOptionalByEmail(dto.getEmail());
        if(!optionalAuth.isEmpty())
            throw new AuthManagerException(ErrorType.DUPLICATE_USER);
        Auth auth = IAuthMapper.INSTANCE.fromVisitorsRequestDtoToAuth(dto);
        auth.setRoles(List.of(ERole.VISITOR));
        if (dto.getPassword().equals(dto.getRepassword())){
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            auth.setStatus(EStatus.PENDING);
            String code= CodeGenerator.generateCode();
            auth.setActivationCode(code);
            save(auth);
            userManager.createVisitorUser(IAuthMapper.INSTANCE.fromAuthNewCreateVisitorUserRequestDto(auth));
            registerMailProducer.sendActivationCode(IAuthMapper.INSTANCE.fromAuthToRegisterMailModel(auth));
            return true;
        }
        throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
    }

    public RegisterResponseDto registerManager(RegisterManagerRequestDto dto){
        Auth auth = IAuthMapper.INSTANCE.fromManagerRequestDtoToAuth(dto);
        auth.setRoles(List.of(ERole.MANAGER,ERole.PERSONEL,ERole.FOUNDER));

//        Boolean isCompanyExists = companyManager.doesCompanyExist(dto.getCompanyId()).getBody();
//        if(!isCompanyExists)
//            throw new AuthManagerException(ErrorType.COMPANY_NOT_FOUND);
//        Boolean isFounderExists = userManager.doesFounderExists(dto.getCompanyId()).getBody();
//        System.out.println(isFounderExists);
//        if(!isFounderExists)
//            throw new AuthManagerException(ErrorType.FOUNDER_EXIST_ERROR);
//        Boolean isSubscriptionExists = companyManager.doesCompanySubscriptionExist(dto.getCompanyId()).getBody();
//        System.out.println("Company Subscription" + isSubscriptionExists);
//        if(!isSubscriptionExists)
//            throw new AuthManagerException(ErrorType.COMPANY_SUBSCRIPTION_EXIST);

        if (dto.getPassword().equals(dto.getRepassword())){
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            NewCreateManagerUserRequestDto managerUserDto = IAuthMapper.INSTANCE.fromRegisterManagerRequestDtoToNewCreateManagerUserRequestDto(dto);
            Long companyId = companyManager.saveCompanyRequestDto(IAuthMapper.INSTANCE.toCompanyRequestDto(managerUserDto)).getBody();
            auth.setCompanyId(companyId);
            save(auth);
            managerUserDto.setAuthId(auth.getAuthId());
            managerUserDto.setPassword(auth.getPassword());
            managerUserDto.setCompanyName(auth.getCompanyName());
            managerUserDto.setTaxNumber(auth.getTaxNumber());
            managerUserDto.setCompanyId(companyId);
            userManager.createManagerUser(managerUserDto);
//            companyManager.subscribeCompany(IAuthMapper.INSTANCE.fromRegisterManagerRequestDtoToSubscribeCompanyRequestDto(dto));
            registerMailHelloProducer.sendHello(IAuthMapper.INSTANCE.fromAuthToRegisterMailHelloModel(auth));
        }else {
            throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
        }
        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.fromAuthToResponseDto(auth);
        return responseDto;
    }

    public LoginResponseDto login(LoginRequestDto dto){
        Optional<Auth> auth=authRepository.findOptionalByEmail(dto.getEmail());
        System.out.println(auth);
        if(auth.isEmpty()||!passwordEncoder.matches(dto.getPassword(), auth.get().getPassword())){
            throw new AuthManagerException(ErrorType.LOGIN_ERROR);
        }
        if(!auth.get().getStatus().equals(EStatus.ACTIVE)){
            throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
        }
        String token = null;
        List<String> roleList = auth.get().getRoles().stream().map(x -> x.toString()).collect(Collectors.toList());
        if (roleList.contains(ERole.MANAGER.name())){
            token = jwtTokenProvider.createManagerToken(auth.get().getAuthId(), auth.get().getCompanyId(),roleList)
                    .orElseThrow(()->{
                        throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
                    });
        }else {
            token = jwtTokenProvider.createAuthToken(auth.get().getAuthId(),roleList,auth.get().getCompanyId())
                    .orElseThrow(()->{
                        throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
                    });
        }
        return LoginResponseDto.builder().roles(roleList).token(token).build();
    }

    public Boolean forgotPasswordRequest(String email){
        Optional<Auth> optionalAuth = authRepository.findOptionalByEmail(email);
        if(optionalAuth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        if(!optionalAuth.get().getStatus().equals(EStatus.ACTIVE))
            throw new RuntimeException("Kullanıcı aktif değil");
        forgotPasswordProducer.sendForgotPassword(ForgotPasswordMailModel.builder()
                        .authId(optionalAuth.get().getAuthId())
                        .email(optionalAuth.get().getEmail())
                .build());
        return true;
    }

    public ConfirmationResponseDto confirmUserAccount(String activationCode) {
            String redirectUrl = "http://localhost:5173/login/login";
        try {
            Auth confirmAcc=authRepository.findOptionalByActivationCode(activationCode).get();
            String temp=confirmAcc.getActivationCode();
            if (activationCode.equals(temp)){
                userManager.activateUser(confirmAcc.getAuthId());
                confirmAcc.setStatus(EStatus.ACTIVE);
                save(confirmAcc);
                // Auth işlemi başarılı olduğunda ConfirmationResponse objesini oluştur ve dön
                return new ConfirmationResponseDto("User account confirmed", HttpStatus.OK.value(), redirectUrl);
            }else{
                throw new AuthManagerException(ErrorType.INVALID_ACTION);
            }
        }catch (Exception e){
            // Hata durumunda da uygun bir response dön
            return new ConfirmationResponseDto("Error confirming user account", HttpStatus.INTERNAL_SERVER_ERROR.value(), redirectUrl);
        }
    }

    public List<Auth> findAll() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return authRepository.findAll();
    }

    public Long managerCreatePersonelUserProfile(AuthCreatePersonnelProfileResponseDto dto){
        Auth auth = IAuthMapper.INSTANCE.fromCreatePersonelProfileDtotoAuth(dto);
        auth.setRoles(Arrays.asList(ERole.PERSONEL));
        save(auth);
        return auth.getAuthId();
    }


    public Boolean forgotPassword(String token, ForgotPasswordChangePasswordRequestDto dto) {

            Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(() -> {
                throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
            });
            if(!dto.getPassword().equals(dto.getRepassword()))
                throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
            Optional<Auth> optionalAuth = findById(authId);
            optionalAuth.get().setPassword(passwordEncoder.encode(dto.getPassword()));
            update(optionalAuth.get());
            resetPasswordProducer.sendResetPassword(ResetPasswordModel.builder()
                    .email(optionalAuth.get().getEmail())
                    .password(dto.getPassword())
                    .build());
            userManager.forgotPassword(IAuthMapper.INSTANCE.fromAuthToForgotPasswordUserRequestDto(optionalAuth.get()));
            return true;

    }

    public Boolean updateManagerStatus(UpdateManagerStatusResponseDto dto) {
        Optional<Auth> auth = findById(dto.getAuthId());
        if(auth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        auth.get().setStatus(dto.getStatus());
        update(auth.get());
        return true;
    }

    public Boolean managerDeletePersonnel(DeletePersonnelFromAuthResponseDto dto) {
        Optional<Auth> auth = findById(dto.getAuthId());
        if(auth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        auth.get().setStatus(dto.getStatus());
        update(auth.get());
        return true;
    }

    public List<String> getRolesFromToken(String token){
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if(roles.isEmpty())
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        return roles;
    }

    public Boolean updateBecauseOfUserProfile(PersonelUpdateUserProfileToAuthRequestDto dto){
        Optional<Long> fromToken = jwtTokenProvider.getAuthIdFromToken(dto.getToken());
        if (fromToken.isEmpty())
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        Optional<Auth> auth = authRepository.findById(fromToken.get());
        if (auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setName(dto.getName());
        auth.get().setSurname(dto.getSurname());
        auth.get().setEmail(dto.getEmail());
        auth.get().setPassword(dto.getPassword());
        update(auth.get());
        return true;
    }

    public Boolean passwordChange(ToAuthPasswordChangeDto dto){
        Optional<Auth> auth = authRepository.findById(dto.getAuthId());
        if (auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setPassword(dto.getPassword());
        update(auth.get());
        return true;
    }

    public Long founderCreateManagerUserProfile(AuthCreatePersonnelProfileResponseDto dto) {
        Auth auth = IAuthMapper.INSTANCE.fromCreatePersonelProfileDtotoAuth(dto);
        auth.setRoles(Arrays.asList(ERole.PERSONEL,ERole.MANAGER));
        save(auth);
        return auth.getAuthId();
    }

    @PostConstruct
    public void defaultAdmin(){
        save(Auth.builder()
                .email("admin@admin.admin")
                .password(passwordEncoder.encode("admin"))
                .roles(List.of(ERole.ADMIN))
                .status(EStatus.ACTIVE)
                .build());
    }

    public List<PendingManagerResponseDtoList> findPendingManagers() {
        List<Auth> pendingManagers = authRepository.findByRolesContainsAndStatus(ERole.MANAGER, EStatus.PENDING);
        return IAuthMapper.INSTANCE.fromAuthListToPendingManagerResponseDtoList(pendingManagers);
    }

    public List<PersonelListResponseDto> getPersonelList(String token) {
        Long companyId = jwtTokenProvider.getCompanyIdFromToken(token).get();
        if (companyId == null) {
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }

        List<Auth> authList = authRepository.findByCompanyId(companyId).get();
        if (authList.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }

        List<PersonelListResponseDto> personnelList = new ArrayList<>();
        for (Auth auth : authList) {
            PersonelListResponseDto dto = IAuthMapper.INSTANCE.fromAuthToPersonelListResponseDto(auth);
            personnelList.add(dto);
        }

        return personnelList;
    }
    public List<PersonelListResponseDto> getPersonelList2() {
        Long companyId = 3L;

        List<Auth> authList = authRepository.findByCompanyId(companyId).get();
        if (authList.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }

        List<PersonelListResponseDto> personnelList = new ArrayList<>();
        for (Auth auth : authList) {
            PersonelListResponseDto dto = IAuthMapper.INSTANCE.fromAuthToPersonelListResponseDto(auth);
            personnelList.add(dto);
        }

        return personnelList;
    }

}
