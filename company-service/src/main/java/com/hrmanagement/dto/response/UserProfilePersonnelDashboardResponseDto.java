package com.hrmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserProfilePersonnelDashboardResponseDto {
    private Long userId;
    private List<String> jobBreak;
    private String jobShift;
    private String department;
    private Double wage;
    private Long companyId;
}
