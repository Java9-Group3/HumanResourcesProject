package com.hrmG3.dto.request;

import com.hrmG3.repository.enums.EExpenseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequestDto {
    private Long expenseId;
    private EExpenseStatus eExpenseStatus;
}
