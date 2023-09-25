package com.hrmG3.mapper;

import com.hrmG3.dto.request.AddCompanyRequestDto;
import com.hrmG3.repository.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICompanyMapper {
    ICompanyMapper INSTANCE = Mappers.getMapper(ICompanyMapper.class);
    Company fromAddCompanyResponseDtoToCompany(final AddCompanyRequestDto dto);
}
