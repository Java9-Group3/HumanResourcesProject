package com.hrmanagement.repository.entity;

import com.hrmanagement.repository.entity.enums.ApprovalStatus;
import com.hrmanagement.repository.entity.enums.EExpenseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Expense extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;
    private Long authid;
    private String companyName;
    private Long companyId;
    private Long amount;
    @Enumerated(EnumType.STRING)
    private EExpenseType expenseType;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;
    @Builder.Default
    private String currency = "TRY";
    @Builder.Default
    private LocalDate requestDate=LocalDate.now();
    private LocalDate replyDate;
    private String file;
}