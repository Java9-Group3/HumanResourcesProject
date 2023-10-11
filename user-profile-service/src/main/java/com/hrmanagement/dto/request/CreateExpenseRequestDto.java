package com.hrmanagement.dto.request;

import com.hrmanagement.repository.entity.enums.EExpenseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateExpenseRequestDto {
    String token;
    Double amount;
    EExpenseType expenseType;
    String currency;
}
