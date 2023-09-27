package com.hrmanagement.service;

import com.hrmanagement.dto.request.*;
import com.hrmanagement.dto.response.*;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.exception.UserProfileManagerException;
import com.hrmanagement.manager.IAuthManager;
import com.hrmanagement.manager.ICompanyManager;
import com.hrmanagement.mapper.IUserProfileMapper;
import com.hrmanagement.rabbitmq.model.ManagerChangeStatusModel;
import com.hrmanagement.rabbitmq.model.PersonnelPasswordModel;
import com.hrmanagement.rabbitmq.producer.ManagerChangeStatusProducer;
import com.hrmanagement.rabbitmq.producer.PersonelPasswordProducer;
import com.hrmanagement.repository.IUserProfileRepository;
import com.hrmanagement.repository.entity.UserProfile;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.repository.entity.enums.EStatus;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class UserProfileService extends ServiceManager<UserProfile, Long> {
    private final IUserProfileRepository userProfileRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IAuthManager authManager;
    private final PersonelPasswordProducer personelPasswordProducer;
    private final ICompanyManager companyManager;
    private final PasswordEncoder passwordEncoder;
    private final ManagerChangeStatusProducer managerChangeStatusProducer;

    private UserProfileService(IUserProfileRepository userProfileRepository, IAuthManager authManager, JwtTokenProvider jwtTokenProvider, PersonelPasswordProducer personelPasswordProducer, ICompanyManager companyManager, PasswordEncoder passwordEncoder, ManagerChangeStatusProducer managerChangeStatusProducer){
        super(userProfileRepository);
        this.userProfileRepository=userProfileRepository;
        this.authManager=authManager;
        this.jwtTokenProvider=jwtTokenProvider;
        this.personelPasswordProducer = personelPasswordProducer;
        this.companyManager = companyManager;
        this.passwordEncoder = passwordEncoder;
        this.managerChangeStatusProducer = managerChangeStatusProducer;

    }

    /**
     *  Personnel Create methodunda dateOfBirth için takvim etkinleştirmesi bakılacak
     *  Gender combobox'ından değer çekilme bakılacak
     *  Base64 formatında avatar ekleme koyulamalıdır.
     *
     *
     *
     *
     * Şifre validasyonu
     *
     *
     * @param token
     * @return
     */
    //çift butonlu olacaktır
    public Boolean adminChangeManagerStatus(String token, ChangeManagerStatusRequestDto dto) {
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(() -> {throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);});
        Optional<UserProfile> optionalAdminProfile = userProfileRepository.findByAuthId(authId);
        List<String> role = jwtTokenProvider.getRoleFromToken(token);
        if(role.contains(ERole.ADMIN.toString())) {
            if (optionalAdminProfile.isEmpty())
                throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
            Optional<UserProfile> user = findById(dto.getUserId());
            if (user.get().getRole().contains(ERole.MANAGER)) {
                if (dto.getAction()) {
                    user.get().setStatus(EStatus.ACTIVE);
                } else {
                    user.get().setStatus(EStatus.BANNED);
                }
                update(user.get());
                authManager.updateManagerStatus(IUserProfileMapper.INSTANCE.fromUserProfileToUpdateManagerStatusRequestDto(user.get()));
                managerChangeStatusProducer.sendInfoMailForManager(ManagerChangeStatusModel.builder()
                        .email(user.get().getEmail())
                        .name(user.get().getName())
                        .status(user.get().getStatus().toString())
                        .build());
                return true;
            }
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        throw new UserProfileManagerException(ErrorType.AUTHORIZATION_ERROR);
    }

    public Boolean forgotPassword(ForgotPasswordUserResponseDto dto) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByAuthId(dto.getAuthId());
        if(optionalUserProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        optionalUserProfile.get().setPassword(dto.getPassword());
        update(optionalUserProfile.get());
        return true;
    }

    public Boolean managerCreatePersonelUserProfile(String token, CreateUserProfileRequestDto dto){
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByEmail(dto.getEmail());
        if(optionalUserProfile.isEmpty()) {

            List<String> role = jwtTokenProvider.getRoleFromToken(token);
            Long managerAuthId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()-> {throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
            Optional<UserProfile> managerProfile = userProfileRepository.findByAuthId(managerAuthId);
            if(managerProfile.isEmpty())
                throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
            if (role.contains(ERole.MANAGER.toString())) {
                UserProfile userProfile = IUserProfileMapper.INSTANCE.fromCreateUserProfileRequestDtoToUserProfile(dto);
                if(dto.getBase64Avatar()!=null){
                    String encodedAvatar = Base64.getEncoder().encodeToString(dto.getBase64Avatar().getBytes());
                    userProfile.setAvatar(encodedAvatar);
                }
                String newPassword = UUID.randomUUID().toString();
                userProfile.setPassword(passwordEncoder.encode(newPassword));
                userProfile.setRole(Arrays.asList(ERole.PERSONEL));
                userProfile.setStatus(EStatus.ACTIVE);
                userProfile.setCompanyId(managerProfile.get().getCompanyId());
                AuthCreatePersonnelProfileRequestDto authDto = IUserProfileMapper.INSTANCE.fromUserProfileToAuthCreatePersonelProfileRequestDto(userProfile);
                Long personnelAuthId = authManager.managerCreatePersonnelUserProfile(authDto).getBody();
                userProfile.setAuthId(personnelAuthId);
                save(userProfile);
                PersonnelPasswordModel personnelPasswordModel = IUserProfileMapper.INSTANCE.fromUserProfileToPersonnelPasswordModel(userProfile);
                personnelPasswordModel.setPassword(newPassword);
                personelPasswordProducer.sendPersonnelPassword(personnelPasswordModel);
                return true;
            }
            throw new UserProfileManagerException(ErrorType.AUTHORIZATION_ERROR);
        }
        throw new UserProfileManagerException(ErrorType.USERNAME_DUPLICATE);
    }

    public Boolean createVisitorUser(NewCreateVisitorUserResponseDto dto) {
        UserProfile userProfile = IUserProfileMapper.INSTANCE.fromNewCreateVisitorUserResponseDtoToUserProfile(dto);
        List<ERole> roleList = new ArrayList<>();
        roleList.add(ERole.VISITOR);
        userProfile.setRole(roleList);
        save(userProfile);
        return true;
    }


    public Boolean createManagerUser(NewCreateManagerUserResponseDto dto) {
        UserProfile userProfile = IUserProfileMapper.INSTANCE.fromNewCreateManagerUserResponseDtoToUserProfile(dto);
        List<ERole> roleList = new ArrayList<>();
        roleList.add(ERole.MANAGER);
        roleList.add(ERole.PERSONEL);
        roleList.add(ERole.FOUNDER);
        userProfile.setStatus(EStatus.PENDING);
        userProfile.setRole(roleList);
        save(userProfile);
        return true;
    }


    public String getCompanyId(Long authId) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByAuthId(authId);
        if(optionalUserProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        return optionalUserProfile.get().getCompanyId();
    }

    public List<String> getManagerNames(String companyId) {
        List<UserProfile> userProfileList = userProfileRepository.findByCompanyId(companyId);
        if(userProfileList.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        List<String> managerList = new ArrayList<>();
        userProfileList.forEach(userProfile -> managerList.add(userProfile.getName()));
        return managerList;
    }

    public Boolean managerDeletePersonnel(String token,Long userId){
        UserProfile personnelprofile = findById(userId).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        Long managerId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        UserProfile managerProfile = userProfileRepository.findByAuthId(managerId).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        List<String> role = jwtTokenProvider.getRoleFromToken(token);
        if(role.contains(ERole.MANAGER.toString()) && managerProfile.getCompanyId().equals(personnelprofile.getCompanyId())){
            if(personnelprofile.getStatus() != EStatus.DELETED) {
                personnelprofile.setStatus(EStatus.DELETED);
                update(personnelprofile);
                authManager.managerDeletePersonnel(IUserProfileMapper.INSTANCE.fromUserProfileToDeletePersonnelFromAuthRequestDto(personnelprofile));
                return true;
            }
            throw new UserProfileManagerException(ErrorType.USER_ALREADY_DELETED);
        }
        if(!managerProfile.getCompanyId().equals(personnelprofile.getCompanyId())){
            throw new UserProfileManagerException(ErrorType.DIFFERENT_COMPANY);
        }
        throw new UserProfileManagerException(ErrorType.AUTHORIZATION_ERROR);
    }
    //BAKILCAK DÜZELTILECEK
    public PersonnelInformationResponseDto showPersonnelInformation(String token){
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if(roles.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        if(roles.contains(ERole.PERSONEL.toString())){
            UserProfile userProfile = userProfileRepository.findByAuthId(authId).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
            PersonnelInformationResponseDto dto = IUserProfileMapper.INSTANCE.fromUserProfileToPersonnelInformationResponseDto(userProfile);
            PersonnelCompanyInformationResponseDto companyDto = companyManager.getPersonnelCompanyInformation(userProfile.getCompanyId()).getBody();
            return IUserProfileMapper.INSTANCE.fromPersonnelCompanyInformationResponseDtoToPersonnelInformationResponseDto(companyDto,dto);
        }
        throw new UserProfileManagerException(ErrorType.AUTHORIZATION_ERROR);
    }

    public Boolean activateUser(Long authId) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByAuthId(authId);
        if(optionalUserProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        optionalUserProfile.get().setStatus(EStatus.ACTIVE);
        update(optionalUserProfile.get());
        return true;
    }

    public UserProfileCommentResponseDto getUserProfileCommentInformation(Long authId) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByAuthId(authId);
        if(optionalUserProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        return IUserProfileMapper.INSTANCE.fromUserProfileToUserProfileCommentResponseDto(optionalUserProfile.get());
    }

    public UserProfileExpenseResponseDto getUserProfileExpenseInformation(Long authId) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByAuthId(authId);
        if(optionalUserProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        return IUserProfileMapper.INSTANCE.fromUserProfileToUserProfileExpenseResponseDto(optionalUserProfile.get());
    }
    public List<FindAllManagerResponseDto> findAllInactiveManager(String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty())
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        Optional<UserProfile> userProfile = userProfileRepository.findByAuthId(authId.get());
        if (userProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);

        if (userProfile.get().getRole().toString().contains("ADMIN")) {
            List<FindAllManagerResponseDto> inactiveManagerList = userProfileRepository.findAllByStatusAndRole(EStatus.INACTIVE,ERole.MANAGER);
            //TODO manager ile companyName alınacak. UNUTMA!!
            inactiveManagerList.forEach(x -> {
                if(x.getAvatar()!=null){
                    try{
                        byte[] decodedBytes = Base64.getDecoder().decode(x.getAvatar());
                        String decodedAvatar = new String(decodedBytes);
                        x.setAvatar(decodedAvatar);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
                String companyName =companyManager.getCompanyNameWithCompanyId(x.getCompanyId()).getBody();
                x.setCompanyName(companyName);
            });
            return inactiveManagerList;
        }
        throw new RuntimeException("Admin değilsin");
    }

    public String getUserAvatarByUserId(Long userId) {
        UserProfile userProfile = findById(userId).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        if(userProfile.getAvatar()!=null){
            try{
                byte[] decodedBytes = Base64.getDecoder().decode(userProfile.getAvatar());
                String decodedAvatar = new String(decodedBytes);
                return decodedAvatar;
            }catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }



    public PersonnelDashboardCommentRequestDto findAllActiveCompanyComments(Long authId) {
        UserProfile userProfile = userProfileRepository.findByAuthId(authId).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        PersonnelDashboardCommentRequestDto dto = IUserProfileMapper.INSTANCE.fromUserProfileToPersonnelDashboardCommentRequestDto(userProfile);
        return dto;
    }

    public UserProfileManagerDashboardRequestDto getUserProfileManagerDashboard(Long authId) {
        UserProfile userProfile = userProfileRepository.findByAuthId(authId).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        Integer companyPersonnelCount = userProfileRepository.countByCompanyId(userProfile.getCompanyId());
        UserProfileManagerDashboardRequestDto dto = UserProfileManagerDashboardRequestDto.builder()
                .companyId(userProfile.getCompanyId())
                .companyPersonnelCount(companyPersonnelCount)
                .build();
        return dto;
    }


    public UserProfileAvatarAndNameResponseDto getUserProfileAvatarAndName(String token) {
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        UserProfile userProfile = userProfileRepository.findByAuthId(authId).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        UserProfileAvatarAndNameResponseDto dto = UserProfileAvatarAndNameResponseDto.builder()
                .name(userProfile.getName())
                .build();
        if(userProfile.getAvatar()!=null){
            try{
                byte[] decodedBytes = Base64.getDecoder().decode(userProfile.getAvatar());
                String decodedAvatar = new String(decodedBytes);
                dto.setAvatar(decodedAvatar);
            }catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return dto;
    }
    public UserProfileAvatarAndNameAndSurnameResponseDto getUserProfileAvatarAndNameAndSurname(String token) {
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        UserProfile userProfile = userProfileRepository.findByAuthId(authId).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        UserProfileAvatarAndNameAndSurnameResponseDto dto = IUserProfileMapper.INSTANCE.fromUserProfileToUserProfileAvatarAndNameAndSurnameResponseDto(userProfile);
        if(userProfile.getAvatar()!=null){
            try{
                byte[] decodedBytes = Base64.getDecoder().decode(userProfile.getAvatar());
                String decodedAvatar = new String(decodedBytes);
                dto.setAvatar(decodedAvatar);
            }catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return dto;
    }
    public Boolean updatePersonel(PersonelUpdateRequestDto personelUpdateRequestDto) {
        Long authId = jwtTokenProvider.getIdFromToken(personelUpdateRequestDto.getToken())
                .orElseThrow(() -> { throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND); });
        List<String> roles = jwtTokenProvider.getRoleFromToken(personelUpdateRequestDto.getToken());
        Optional<UserProfile> personelprofile = userProfileRepository.findByAuthId(authId);

        if (roles.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }

        if (roles.contains(ERole.PERSONEL.toString())||roles.contains(ERole.VISITOR.toString())) {
            if (personelprofile.isPresent()) {
                UserProfile profile = personelprofile.get();
                profile.setName(personelUpdateRequestDto.getName());
                profile.setMiddleName(personelUpdateRequestDto.getMiddleName());
                profile.setSurname(personelUpdateRequestDto.getSurname());
                profile.setEmail(personelUpdateRequestDto.getEmail());
                profile.setPhone(personelUpdateRequestDto.getPhone());

                PersonelUpdateUserProfileToAuthRequestDto dto=IUserProfileMapper.INSTANCE.fromPersonelUpdateRequestDtoToPersonelUpdateUserProfileToAuthRequestDto(personelUpdateRequestDto);
                authManager.updatePersonel(dto);
                update(profile);
                return true;
            } else {
                throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
            }
        } else {
            throw new UserProfileManagerException(ErrorType.NOT_PERSONEL);
        }
    }
    public UserProfileSendingInfosResponseDto getPersonelProfileForUserProfileDashboard(String token) {
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(() -> {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        });
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (roles.isEmpty())
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);

        UserProfile userProfile = userProfileRepository.findByAuthId(authId).orElseThrow(() -> {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        });
        AllCompanyInfosForUserProfileResponseDto companyInfos =
                companyManager.getAllInfosCompanyWithCompanyId(userProfile.getCompanyId()).getBody();
        UserProfileSendingInfosResponseDto dto=IUserProfileMapper.INSTANCE.userProfileToUserProfileSendingInfosResponseDto(userProfile);
        dto.setCompanyName(companyInfos.getCompanyName());
        dto.setCompanyNeighbourhood(companyInfos.getCompanyNeighbourhood());
        dto.setCompanyDistrict(companyInfos.getCompanyDistrict());
        dto.setCompanyProvince(companyInfos.getCompanyProvince());
        dto.setCompanyCountry(companyInfos.getCompanyCountry());
        dto.setCompanyBuildingNumber(companyInfos.getCompanyBuildingNumber());
        dto.setCompanyApartmentNumber(companyInfos.getCompanyApartmentNumber());
        dto.setCompanyPostalCode(companyInfos.getCompanyPostalCode());
        if(userProfile.getAvatar()!=null){
            try{
                byte[] decodedBytes = Base64.getDecoder().decode(userProfile.getAvatar());
                String decodedAvatar = new String(decodedBytes);
                dto.setAvatar(decodedAvatar);
            }catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return dto;
    }
    public Boolean updatePersonelAdress(PersonelAddressUpdateRequestDto personelUpdateRequestDto) {
        Long authId = jwtTokenProvider.getIdFromToken(personelUpdateRequestDto.getToken())
                .orElseThrow(() -> { throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND); });
        List<String> roles = jwtTokenProvider.getRoleFromToken(personelUpdateRequestDto.getToken());
        Optional<UserProfile> personelprofile = userProfileRepository.findByAuthId(authId);

        if (roles.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }

        if (roles.contains(ERole.PERSONEL.toString())||roles.contains(ERole.VISITOR.toString())) {
            if (personelprofile.isPresent()) {
                UserProfile profile = personelprofile.get();
                profile.setNeighbourhood(personelUpdateRequestDto.getNeighbourhood());
                profile.setDistrict(personelUpdateRequestDto.getDistrict());
                profile.setProvince(personelUpdateRequestDto.getProvince());
                profile.setCountry(personelUpdateRequestDto.getCountry());
                profile.setBuildingNumber(personelUpdateRequestDto.getBuildingNumber());
                profile.setApartmentNumber(personelUpdateRequestDto.getApartmentNumber());
                profile.setPostalCode(personelUpdateRequestDto.getPostalCode());

                update(profile);
                return true;
            } else {
                throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
            }
        } else {
            throw new UserProfileManagerException(ErrorType.NOT_PERSONEL);
        }
    }
    public String passwordChange(PasswordChangeDto dto){

        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()){
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findByAuthId(authId.get());
        if (userProfile.isPresent()){
            if (passwordEncoder.matches(dto.getOldPassword(), userProfile.get().getPassword())){
                String newPass = dto.getNewPassword();
                userProfile.get().setPassword(passwordEncoder.encode(newPass));
                userProfileRepository.save(userProfile.get());
                authManager.passwordChange(IUserProfileMapper.INSTANCE.fromUserProfileToAuthPasswordChangeDto(userProfile.get()));

                return dto.getNewPassword();
            }else {
                throw new UserProfileManagerException(ErrorType.PASSWORD_ERROR);
            }
        }else {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
    }

    public Boolean doesFounderExists(String companyId) {
        List<UserProfile> userProfileList = userProfileRepository.findByCompanyId(companyId);
        if(userProfileList.isEmpty())
            return true;
        for(UserProfile userProfile : userProfileList){
            if(userProfile.getRole().contains(ERole.FOUNDER.toString()))
                return false;
        }
        return true;
    }
    public Double getWage(String token){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()){
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }
        UserProfile userProfile = userProfileRepository.findByAuthId(authId.get()).orElseThrow(
                ()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        return userProfile.getWage();
    }

    public Boolean founderCreateManagerUserProfile(String token, CreateUserProfileRequestDto dto){
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByEmail(dto.getEmail());
        if(optionalUserProfile.isEmpty()) {
            List<String> role = jwtTokenProvider.getRoleFromToken(token);
            Long founderAuthId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()-> {throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
            Optional<UserProfile> managerProfile = userProfileRepository.findByAuthId(founderAuthId);
            if(managerProfile.isEmpty())
                throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
            if (role.contains(ERole.FOUNDER.toString())) {
                UserProfile userProfile = IUserProfileMapper.INSTANCE.fromCreateUserProfileRequestDtoToUserProfile(dto);
                if(dto.getBase64Avatar()!=null){
                    String encodedAvatar = Base64.getEncoder().encodeToString(dto.getBase64Avatar().getBytes());
                    userProfile.setAvatar(encodedAvatar);
                }
                String newPassword = UUID.randomUUID().toString();
                userProfile.setPassword(passwordEncoder.encode(newPassword));
                userProfile.setRole(Arrays.asList(ERole.PERSONEL,ERole.MANAGER));
                userProfile.setStatus(EStatus.ACTIVE);
                userProfile.setCompanyId(managerProfile.get().getCompanyId());
                AuthCreatePersonnelProfileRequestDto authDto = IUserProfileMapper.INSTANCE.fromUserProfileToAuthCreatePersonelProfileRequestDto(userProfile);
                Long personnelAuthId = authManager.founderCreateManagerUserProfile(authDto).getBody();
                userProfile.setAuthId(personnelAuthId);
                save(userProfile);
                PersonnelPasswordModel personnelPasswordModel = IUserProfileMapper.INSTANCE.fromUserProfileToPersonnelPasswordModel(userProfile);
                personnelPasswordModel.setPassword(newPassword);
                personelPasswordProducer.sendPersonnelPassword(personnelPasswordModel);
                return true;
            }
            throw new UserProfileManagerException(ErrorType.AUTHORIZATION_ERROR);
        }
        throw new UserProfileManagerException(ErrorType.USERNAME_DUPLICATE);
    }

    public String findAvatar(Long userId) {
        UserProfile userProfile = findById(userId).orElseThrow(()->{
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        });
        if(userProfile.getAvatar()!=null && userProfile.getAvatar()!=""){
            try{
                byte[] decodedBytes = Base64.getDecoder().decode(userProfile.getAvatar());
                String decodedAvatar = new String(decodedBytes);
                return decodedAvatar;
            }catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    public UserProfile findByAuthId(Long authId) {
        Optional<UserProfile> userProfile = userProfileRepository.findByAuthId(authId);
        if (userProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        return userProfile.get();
    }


    public Boolean updateAvatar(String token, UpdateAvatarRequestDto dto){
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        });
        System.out.println(dto);
        if(dto.getBase64Img()!=null){
            UserProfile userProfile = userProfileRepository.findByAuthId(authId).orElseThrow(()->{
                throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
            });
            String encodedAvatar = Base64.getEncoder().encodeToString(dto.getBase64Img().getBytes());
            userProfile.setAvatar(encodedAvatar);
            update(userProfile);
            return true;
        }
        return false;
    }




}





