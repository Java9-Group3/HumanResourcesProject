package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicHolidaysRequestDto {
    private String date;
    private String description;
    private Long id;
    private String name;
    //push deneme
}
