package com.hrmG3.repository.entity;

import com.hrmG3.repository.enums.EExpenseStatus;
import lombok.*;

import javax.persistence.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tblExpense")
public class Expense extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;
    private Long userId;
    private String name;
    private String surname;
    private Long companyId;
    private String expenseType;
    private String currency;
    private String demandDate;
    private String billDate;
    private String paymentMethod;
    private Double amount;
    private Double netAmount;
    private Double tax;
    private String taxZone;
    private String description;
    private String billPhoto;
    @Builder.Default
    private EExpenseStatus eExpenseStatus = EExpenseStatus.PENDING;
}
