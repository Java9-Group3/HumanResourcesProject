package com.hrmG3.mapper;

import com.hrmG3.dto.request.*;
import com.hrmG3.dto.response.RegisterResponseDto;
import com.hrmG3.rabbitmq.model.RegisterMailHelloModel;
import com.hrmG3.rabbitmq.model.RegisterMailModel;
import com.hrmG3.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {

    IAuthMapper INSTANCE = Mappers.getMapper(IAuthMapper.class);

    Auth fromVisitorsRequestDtoToAuth(final RegisterVisitorRequestDto dto);

    NewCreateVisitorUserRequestDto fromAuthToNewCreateUserDto(final RegisterManagerRequestDto dto);

    RegisterResponseDto fromAuthToResponseDto(final Auth auth);

    Auth fromManagerRequestDtoToAuth(final RegisterManagerRequestDto dto);

    //ForgotPasswordUserRequestDto fromAuthToForgotPasswordUserRequestDto(final Auth auth);

    RegisterMailHelloModel fromAuthToRegisterMailHelloModel(final Auth auth);

    //Auth fromCreatePersonelProfileDtotoAuth(final AuthCreatePersonnelProfileResponseDto dto);


    RegisterMailModel fromAuthToRegisterMailModel(final Auth auth);

    NewCreateVisitorUserRequestDto fromAuthNewCreateVisitorUserRequestDto(final Auth auth);

    NewCreateManagerUserRequestDto fromRegisterManagerRequestDtoToNewCreateManagerUserRequestDto(final RegisterManagerRequestDto dto);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateBecauseOfUserProfile(PersonelUpdateUserProfileToAuthRequestDto dto, @MappingTarget Auth auth);

    SubscribeCompanyRequestDto fromRegisterManagerRequestDtoToSubscribeCompanyRequestDto(final RegisterManagerRequestDto dto);
}
