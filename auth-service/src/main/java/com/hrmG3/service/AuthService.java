package com.hrmG3.service;


import com.hrmG3.dto.request.LoginRequestDto;
import com.hrmG3.dto.request.NewCreateManagerUserRequestDto;
import com.hrmG3.dto.request.RegisterManagerRequestDto;
import com.hrmG3.dto.request.RegisterVisitorRequestDto;
import com.hrmG3.dto.response.LoginResponseDto;
import com.hrmG3.dto.response.RegisterResponseDto;
import com.hrmG3.exception.AuthManagerException;
import com.hrmG3.exception.ErrorType;
//import com.hrmG3.manager.ICompanyManager;
import com.hrmG3.manager.IUserProfileManager;
import com.hrmG3.mapper.IAuthMapper;
import com.hrmG3.rabbitmq.producer.ForgotPasswordProducer;
import com.hrmG3.rabbitmq.producer.RegisterMailHelloProducer;
import com.hrmG3.rabbitmq.producer.RegisterMailProducer;
import com.hrmG3.rabbitmq.producer.ResetPasswordProducer;
import com.hrmG3.repository.IAuthRepository;
import com.hrmG3.repository.entity.Auth;
import com.hrmG3.repository.entity.enums.ERole;
import com.hrmG3.repository.entity.enums.EStatus;
import com.hrmG3.utility.JwtTokenProvider;
import com.hrmG3.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hrmG3.utility.CodeGenerator.generateCode;

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
//    private final ICompanyManager companyManager;
    public AuthService(IAuthRepository authRepository, PasswordEncoder passwordEncoder, RegisterMailHelloProducer registerMailHelloProducer, IUserProfileManager userManager, JwtTokenProvider jwtTokenProvider, ForgotPasswordProducer forgotPasswordProducer, RegisterMailProducer registerMailProducer, ResetPasswordProducer resetPasswordProducer) {
        super(authRepository);
        this.authRepository=authRepository;
        this.passwordEncoder = passwordEncoder;
        this.registerMailHelloProducer = registerMailHelloProducer;
        this.userManager = userManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.forgotPasswordProducer = forgotPasswordProducer;
        this.registerMailProducer = registerMailProducer;
        this.resetPasswordProducer = resetPasswordProducer;
//        this.companyManager = companyManager;
    }


    public Boolean registerVisitor(RegisterVisitorRequestDto dto){
        Optional<Auth> optionalAuth = authRepository.findOptionalByEmail(dto.getEmail());
        if(!optionalAuth.isEmpty())
            throw new AuthManagerException(ErrorType.DUPLICATE_USER);
        Auth auth = IAuthMapper.INSTANCE.fromVisitorsRequestDtoToAuth(dto);
        auth.setRoles(List.of(ERole.VISITOR));
        if (dto.getPassword().equals(dto.getRepassword())){
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            auth.setStatus(EStatus.ACTIVE);
            save(auth);
            userManager.createVisitorUser(IAuthMapper.INSTANCE.fromAuthNewCreateVisitorUserRequestDto(auth));
            registerMailProducer.sendActivationCode(IAuthMapper.INSTANCE.fromAuthToRegisterMailModel(auth));
            //registerMailHelloProducer.sendHello(IAuthMapper.INSTANCE.fromAuthToRegisterMailHelloModel(auth));
            return true;
        }
        throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
    }

    public RegisterResponseDto registerManager(RegisterManagerRequestDto dto){
        Auth auth = IAuthMapper.INSTANCE.fromManagerRequestDtoToAuth(dto);
        auth.setRoles(List.of(ERole.MANAGER,ERole.PERSONEL,ERole.FOUNDER));
//      C
        if (dto.getPassword().equals(dto.getRepassword())){
            auth.setActivationCode(generateCode());
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            save(auth);
            NewCreateManagerUserRequestDto managerUserDto = IAuthMapper.INSTANCE.fromRegisterManagerRequestDtoToNewCreateManagerUserRequestDto(dto);
            managerUserDto.setAuthId(auth.getAuthId());
            managerUserDto.setPassword(auth.getPassword());
//      C
            userManager.createManagerUser(managerUserDto);
            registerMailProducer.sendActivationCode(IAuthMapper.INSTANCE.fromAuthToRegisterMailModel(auth));
        }else {
            throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
        }
        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.fromAuthToResponseDto(auth);
        return responseDto;
    }

    public LoginResponseDto login(LoginRequestDto dto){
        Optional<Auth> auth=authRepository.findOptionalByEmail(dto.getEmail());
        System.out.println("Bilmiyorum");
        System.out.println(auth);
        if(auth.isEmpty()||!passwordEncoder.matches(dto.getPassword(), auth.get().getPassword())){
            throw new AuthManagerException(ErrorType.LOGIN_ERROR);
        }
        System.out.println("Biliyorum");
        if(!auth.get().getStatus().equals(EStatus.ACTIVE)){
            throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
        }
        List<String> roleList = auth.get().getRoles().stream().map(x -> x.toString()).collect(Collectors.toList());
        System.out.println(roleList);
        String token = jwtTokenProvider.createToken(auth.get().getAuthId(),roleList)
                .orElseThrow(()->{
                    throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
                });
        System.out.println(token);
        return LoginResponseDto.builder().roles(roleList).token(token).build();
    }

    public List<Auth> findAll() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return authRepository.findAll();
    }

}
