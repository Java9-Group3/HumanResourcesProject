package com.hrmG3.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.hrmG3.repository.enums.EStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCreateVisitorUserRequestDto {
    private Long authId;
    private String password;
    private String email;
    private String name;
    private String middleName;
    private String surname;
    private EStatus status;
}
