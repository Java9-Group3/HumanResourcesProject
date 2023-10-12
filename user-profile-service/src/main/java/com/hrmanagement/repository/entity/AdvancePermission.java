package com.hrmanagement.repository.entity;

import com.hrmanagement.repository.entity.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AdvancePermission extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long advanceId;
    private String description;
    private Double amount;
    private Long authid;
    private Long companyId;
    private String name;
    @Builder.Default
    private Long requestDate=System.currentTimeMillis();
    @Builder.Default
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
    @Builder.Default
    private String currency = "TRY";
}
