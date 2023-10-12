package com.hrmanagement.mapper;


import com.hrmanagement.dto.request.CreateAdvancePermissionRequestDto;
import com.hrmanagement.dto.response.ListAdvanceResponseDto;
import com.hrmanagement.repository.entity.AdvancePermission;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAdvanceMapper {

    IAdvanceMapper INSTANCE = Mappers.getMapper(IAdvanceMapper.class);

    AdvancePermission toAdvance(final CreateAdvancePermissionRequestDto dto);
    ListAdvanceResponseDto toDto(final AdvancePermission advancePermission);
}
