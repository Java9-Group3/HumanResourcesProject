package com.hrmG3.mapper;

import com.hrmG3.dto.response.NewCreateManagerUserResponseDto;
import com.hrmG3.dto.response.NewCreateVisitorUserResponseDto;
import com.hrmG3.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserProfileMapper {

    IUserProfileMapper INSTANCE = Mappers.getMapper(IUserProfileMapper.class);

    UserProfile fromNewCreateVisitorUserResponseDtoToUserProfile(final NewCreateVisitorUserResponseDto dto);

    UserProfile fromNewCreateManagerUserResponseDtoToUserProfile(final NewCreateManagerUserResponseDto dto);

}
