package com.hrmG3.service;


import com.hrmG3.dto.response.NewCreateManagerUserResponseDto;
import com.hrmG3.dto.response.NewCreateVisitorUserResponseDto;
import com.hrmG3.exception.ErrorType;
import com.hrmG3.exception.UserProfileManagerException;
import com.hrmG3.manager.IAuthManager;
import com.hrmG3.mapper.IUserProfileMapper;
import com.hrmG3.rabbitmq.producer.ManagerChangeStatusProducer;
import com.hrmG3.rabbitmq.producer.PersonelPasswordProducer;
import com.hrmG3.repository.IUserProfileRepository;
import com.hrmG3.repository.entity.UserProfile;
import com.hrmG3.repository.entity.enums.ERole;
import com.hrmG3.repository.entity.enums.EStatus;
import com.hrmG3.utility.JwtTokenProvider;
import com.hrmG3.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService extends ServiceManager<UserProfile, Long> {
    private final IUserProfileRepository userProfileRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IAuthManager authManager;
    private final PersonelPasswordProducer personelPasswordProducer;
//    private final ICompanyManager companyManager;
    private final PasswordEncoder passwordEncoder;
    private final ManagerChangeStatusProducer managerChangeStatusProducer;

    private UserProfileService(IUserProfileRepository userProfileRepository, IAuthManager authManager, JwtTokenProvider jwtTokenProvider, PersonelPasswordProducer personelPasswordProducer, PasswordEncoder passwordEncoder, ManagerChangeStatusProducer managerChangeStatusProducer){
        super(userProfileRepository);
        this.userProfileRepository=userProfileRepository;
        this.authManager=authManager;
        this.jwtTokenProvider=jwtTokenProvider;
        this.personelPasswordProducer = personelPasswordProducer;
//        this.companyManager = companyManager;
        this.passwordEncoder = passwordEncoder;
        this.managerChangeStatusProducer = managerChangeStatusProducer;

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
        userProfile.setStatus(EStatus.PENDING);
        userProfile.setRole(roleList);
        save(userProfile);
        return true;
    }

    public UserProfile findByAuthId(Long authId) {
        Optional<UserProfile> userProfile = userProfileRepository.findByAuthId(authId);
        if (userProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        return userProfile.get();
    }

}





