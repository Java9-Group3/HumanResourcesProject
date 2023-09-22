package com.hrmG3.mapper;

import com.hrmG3.dto.request.NewCreateVisitorUserRequestDto;
import com.hrmG3.dto.request.RegisterManagerRequestDto;
import com.hrmG3.dto.request.RegisterVisitorRequestDto;
import com.hrmG3.rabbitmq.model.RegisterMailHelloModel;
import com.hrmG3.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = "spring")
public interface IAuthMapper {

    IAuthMapper INSTANCE = Mappers.getMapper(IAuthMapper.class);

    Auth fromVisitorsRequestDtoToAuth(final RegisterVisitorRequestDto dto);

    RegisterMailHelloModel fromAuthToRegisterMailHelloModel(final Auth auth);

    Auth fromManagerRequestDtoToAuth(final RegisterManagerRequestDto dto);
    NewCreateVisitorUserRequestDto fromAuthNewCreateVisitorUserRequestDto(final Auth auth);
}
