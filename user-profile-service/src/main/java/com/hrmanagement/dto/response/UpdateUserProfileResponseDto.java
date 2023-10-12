package com.hrmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileResponseDto {
    private Long id;
    private String name;
    private String surname;
    private String password;
    private String email;
    private String phone;
    private String wage;
    private String jobBreak;
    private String jobShift;
}
