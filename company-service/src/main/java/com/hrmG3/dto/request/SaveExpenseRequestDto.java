package com.hrmG3.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveExpenseRequestDto {
    private Long expenseId;
    private Long userId;
    private String name;
    private String surname;
    private Long companyId;
}
