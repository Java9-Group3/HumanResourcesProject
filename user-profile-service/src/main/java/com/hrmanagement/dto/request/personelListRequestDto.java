package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class personelListRequestDto {
    private String email;
    private String name;
    private String middleName;
    private String surname;
    //deneme
}
