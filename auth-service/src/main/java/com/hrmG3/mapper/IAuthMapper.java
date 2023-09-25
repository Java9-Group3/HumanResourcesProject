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

    /**
     * Ziyaretçi kullanıcı kaydı isteğini Auth nesnesine dönüştürür.
     * @param dto Ziyaretçi kullanıcı kaydı isteği DTO'su.
     * @return Auth nesnesi.
     */
    Auth fromVisitorsRequestDtoToAuth(final RegisterVisitorRequestDto dto);

    /**
     * Auth nesnesini RegisterMailHelloModel'e dönüştürür.
     * @param auth Auth nesnesi.
     * @return RegisterMailHelloModel nesnesi.
     */
    RegisterMailHelloModel fromAuthToRegisterMailHelloModel(final Auth auth);

    /**
     * Yönetici kaydı isteğini Auth nesnesine dönüştürür.
     * @param dto Yönetici kaydı isteği DTO'su.
     * @return Auth nesnesi.
     */
    Auth fromManagerRequestDtoToAuth(final RegisterManagerRequestDto dto);

    /**
     * Auth nesnesini NewCreateVisitorUserRequestDto'ya dönüştürür.
     * @param auth Auth nesnesi.
     * @return NewCreateVisitorUserRequestDto nesnesi.
     */
    NewCreateVisitorUserRequestDto fromAuthNewCreateVisitorUserRequestDto(final Auth auth);
}
