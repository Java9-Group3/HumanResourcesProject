package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class PersonelUpdateUserProfileToAuthRequestDto {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String token;
}
