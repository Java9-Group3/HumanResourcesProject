package com.hrmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserProfileExpenseResponseDto {

    private String userId;
    private String name;
    private String surname;
    private Long companyId;
}
