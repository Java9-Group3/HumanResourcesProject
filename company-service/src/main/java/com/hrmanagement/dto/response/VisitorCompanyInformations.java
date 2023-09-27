package com.hrmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class VisitorCompanyInformations {
    private Long companyId;
    private String logo;
    private String companyName;
    private String companyProvince;
    private String companyCountry;
}
