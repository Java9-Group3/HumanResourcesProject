package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class SaveCompanyRequestDto {
    private String companyName;
    private String sector;
    private String taxNumber;
    private double companyBalanceStatus;
    private String companyPhone;
    private String companyMail;
    private String description;
    private String companyNeighbourhood;
    private String companyDistrict;
    private String companyProvince;
    private String companyCountry;
    private Integer companyBuildingNumber;
    private Integer companyApartmentNumber;
    private Integer companyPostalCode;
    private String base64Logo;
    private List<String> holidayDates;
    private String wageDate;
}
