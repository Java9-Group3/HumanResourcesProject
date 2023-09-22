package com.hrmG3.service;


import com.hrmG3.repository.entity.UserProfile;
import com.hrmG3.dto.response.NewCreateVisitorUserResponseDto;
import com.hrmG3.mapper.IUserProfileMapper;
import com.hrmG3.repository.IUserProfileRepository;
import com.hrmG3.repository.entity.ERole;
import com.hrmG3.utility.ServiceManager;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final IUserProfileRepository userProfileRepository;

    private UserProfileService(IUserProfileRepository userProfileRepository){
        super(userProfileRepository);
        this.userProfileRepository=userProfileRepository;
    }

    public Boolean createVisitorUser(NewCreateVisitorUserResponseDto dto) {
        UserProfile userProfile = IUserProfileMapper.INSTANCE.fromNewCreateVisitorUserResponseDtoToUserProfile(dto);
        List<ERole> roleList = new ArrayList<>();
        roleList.add(ERole.VISITOR);
        save(userProfile);
        return true;
    }





}





