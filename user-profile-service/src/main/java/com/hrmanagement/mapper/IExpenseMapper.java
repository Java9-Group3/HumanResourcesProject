package com.hrmanagement.mapper;


import com.hrmanagement.dto.request.CreateExpenseRequestDto;
import com.hrmanagement.dto.response.ListExpenseResponseDto;
import com.hrmanagement.repository.entity.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IExpenseMapper {

    IExpenseMapper INSTANCE = Mappers.getMapper(IExpenseMapper.class);

    Expense toExpense(final CreateExpenseRequestDto dto);
    ListExpenseResponseDto toDto(final Expense expense);
}
