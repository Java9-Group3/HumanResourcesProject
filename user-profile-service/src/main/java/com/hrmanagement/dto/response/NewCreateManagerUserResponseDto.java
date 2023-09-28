package com.hrmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class NewCreateManagerUserResponseDto {
    private Long authId;
    private String name;
    private String middleName;
    private String surname;
    private String identificationNumber;
    private String email;
    private String password;
    private String repassword;
    private Long companyId;
    private String department;
}
