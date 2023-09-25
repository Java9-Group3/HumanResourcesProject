package com.hrmG3.service;


import com.hrmG3.dto.response.NewCreateManagerUserResponseDto;
import com.hrmG3.dto.response.NewCreateVisitorUserResponseDto;
import com.hrmG3.mapper.IUserProfileMapper;
import com.hrmG3.repository.IUserProfileRepository;
import com.hrmG3.repository.entity.UserProfile;
import com.hrmG3.repository.entity.enums.ERole;
import com.hrmG3.repository.entity.enums.EStatus;
import com.hrmG3.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final IUserProfileRepository userProfileRepository;

    private UserProfileService(IUserProfileRepository userProfileRepository){
        super(userProfileRepository);
        this.userProfileRepository=userProfileRepository;
    }

    /**
     * Yeni bir ziyaretçi kullanıcısı oluşturur.
     * @param dto Yeni kullanıcı bilgilerini içeren DTO (Data Transfer Object).
     * @return Başarılı bir şekilde kullanıcı oluşturulursa true, aksi takdirde false döner.
     */
    public Boolean createVisitorUser(NewCreateVisitorUserResponseDto dto) {
        UserProfile userProfile = IUserProfileMapper.INSTANCE.fromNewCreateVisitorUserResponseDtoToUserProfile(dto);
        List<ERole> roleList = new ArrayList<>();
        roleList.add(ERole.VISITOR);
        save(userProfile);
        return true;
    }

    /**
     * Yeni bir yönetici kullanıcısı oluşturur.
     * @param dto Yeni kullanıcı bilgilerini içeren DTO (Data Transfer Object).
     * @return Başarılı bir şekilde kullanıcı oluşturulursa true, aksi takdirde false döner.
     */
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
}





