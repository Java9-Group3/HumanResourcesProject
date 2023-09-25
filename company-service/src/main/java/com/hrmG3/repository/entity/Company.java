package com.hrmG3.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tblCompany")
public class Company extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;
    private String companyName;
    private String companyNeighbourhood;
    private String companyDistrict;
    private String companyProvince;
    private String companyCountry;
    private Long companyBuildingNumber;
    private Long companyApartmentNumber;
    private Long companyPostalCode;
    private Double companyBalanceStatus;
    private String logo;
    private String taxNumber;
    private String sector;
    private List<String> holidayDates;
    private String description;
    private String companyPhone;
    private String companyMail;
    private String wageDate;
    private Long subscriptionExpirationDate;
}
