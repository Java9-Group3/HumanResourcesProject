package com.hrmanagement.mapper;

import com.hrmanagement.dto.request.AdvancedRequestDto;
import com.hrmanagement.dto.response.FindAllAdvanceRequestListResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface IAdvancePermissionMapper {
    IAdvancePermissionMapper INSTANCE = Mappers.getMapper(IAdvancePermissionMapper.class);
}
