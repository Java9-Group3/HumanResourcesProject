package com.hrmanagement.dto.request;

import com.hrmanagement.repository.entity.enums.EPermissionTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TakeDayOffPermissionRequestDto {
    private String startingDate;
    private String endingDate;

    private String description;
    private EPermissionTypes ePermissionTypes;
}
