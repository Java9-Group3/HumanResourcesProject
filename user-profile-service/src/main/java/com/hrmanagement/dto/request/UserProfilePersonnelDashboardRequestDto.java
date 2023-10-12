package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserProfilePersonnelDashboardRequestDto {
    private Long userId;
    private String jobBreak;
    private String jobShift;
    private String department;
    private Double wage;
    private String wageDate;
    private int employeeCount;
    private Long companyId;

}
