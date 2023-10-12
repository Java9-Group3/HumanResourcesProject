package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class PersonelUpdateRequestDto {
//    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String token;
    private String phone;
    private Double wage;
    private String jobBreak;
    private String jobShift;

}
