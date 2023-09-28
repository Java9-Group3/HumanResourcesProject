package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterManagerRequestDto {

    private String name;
    private String middleName;
    private String surname;
    private String identificationNumber;
    private String email;
    private String password;

    private String repassword;

    private Long companyId;

    private String department;

    private Integer companySubscribeDay;
}
