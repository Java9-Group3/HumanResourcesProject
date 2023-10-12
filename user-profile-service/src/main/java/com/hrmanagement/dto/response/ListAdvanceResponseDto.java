package com.hrmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListAdvanceResponseDto {
    private String description;
    private Double amount;
    private String currency;
    private Long requestDate;
    private Long authid;
    private String name;
    private Long advanceId;
}
