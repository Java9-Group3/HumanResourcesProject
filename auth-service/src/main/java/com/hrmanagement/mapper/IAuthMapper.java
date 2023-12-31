package com.hrmanagement.mapper;

import com.hrmanagement.dto.request.*;
import com.hrmanagement.dto.response.AuthCreatePersonnelProfileResponseDto;
import com.hrmanagement.dto.response.PendingManagerResponseDtoList;
import com.hrmanagement.dto.response.PersonelListResponseDto;
import com.hrmanagement.dto.response.RegisterResponseDto;
import com.hrmanagement.rabbitmq.model.RegisterMailHelloModel;
import com.hrmanagement.rabbitmq.model.RegisterMailModel;
import com.hrmanagement.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {
    IAuthMapper INSTANCE = Mappers.getMapper(IAuthMapper.class);

    List<PendingManagerResponseDtoList> fromAuthListToPendingManagerResponseDtoList(List<Auth> pendingManagers);

    Auth fromVisitorsRequestDtoToAuth(final RegisterVisitorRequestDto dto);

    NewCreateVisitorUserRequestDto fromAuthToNewCreateUserDto(final RegisterManagerRequestDto dto);

    RegisterResponseDto fromAuthToResponseDto(final Auth auth);

    Auth fromManagerRequestDtoToAuth(final RegisterManagerRequestDto dto);

    ForgotPasswordUserRequestDto fromAuthToForgotPasswordUserRequestDto(final Auth auth);

    RegisterMailHelloModel fromAuthToRegisterMailHelloModel(final Auth auth);

    Auth fromCreatePersonelProfileDtotoAuth(final AuthCreatePersonnelProfileResponseDto dto);
    PersonelListResponseDto fromAuthToPersonelListResponseDto(final Auth auth);
    AuthCreatePersonnelProfileResponseDto  fromAuthToCreatePersonelProfileResponseDto(final Auth auth);
    RegisterMailModel fromAuthToRegisterMailModel(final Auth auth);

    NewCreateVisitorUserRequestDto fromAuthNewCreateVisitorUserRequestDto(final Auth auth);

    NewCreateManagerUserRequestDto fromRegisterManagerRequestDtoToNewCreateManagerUserRequestDto(final RegisterManagerRequestDto dto);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateBecauseOfUserProfile(PersonelUpdateUserProfileToAuthRequestDto dto, @MappingTarget Auth auth);

//    SubscribeCompanyRequestDto fromRegisterManagerRequestDtoToSubscribeCompanyRequestDto(final RegisterManagerRequestDto dto);

    SaveCompanyRequestDto toCompanyRequestDto(final NewCreateManagerUserRequestDto managerUserDto);

}
