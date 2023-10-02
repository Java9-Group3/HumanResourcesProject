package com.hrmanagement.dto.response;

import com.hrmanagement.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingManagerResponseDtoList {

    private Long authId;
    private String name;
    private String surname;
    private EStatus status;
}
