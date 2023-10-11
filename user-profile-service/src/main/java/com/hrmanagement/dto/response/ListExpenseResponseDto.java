package com.hrmanagement.dto.response;

import com.hrmanagement.repository.entity.enums.ApprovalStatus;
import com.hrmanagement.repository.entity.enums.EExpenseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListExpenseResponseDto {
    private String companyName;
    private Long amount;
    private EExpenseType expenseType;
    private ApprovalStatus approvalStatus;
    private String currency;
    private LocalDate requestDate;
    @Builder.Default
    private LocalDate replyDate=LocalDate.now();
    private Long companyId;
    private Long expenseId;
}
