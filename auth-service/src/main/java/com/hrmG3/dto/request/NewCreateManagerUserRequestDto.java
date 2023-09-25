package com.hrmG3.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCreateManagerUserRequestDto {
    private Long authId;
    private String name;
    private String middleName;
    private String surname;
    private String identificationNumber;
    private String email;
    private String password;
    private String repassword;
    private String companyId;
    private String department;
}
