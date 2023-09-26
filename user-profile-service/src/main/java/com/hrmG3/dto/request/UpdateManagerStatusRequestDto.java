package com.hrmG3.dto.request;

import com.hrmG3.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateManagerStatusRequestDto {

    private Long authId;
    private EStatus status;
}
