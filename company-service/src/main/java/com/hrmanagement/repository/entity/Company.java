package com.hrmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;


@SuperBuilder
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
    private Integer companyBuildingNumber;
    private Integer companyApartmentNumber;
    private Integer companyPostalCode;
    private Double companyBalanceStatus;
    private String logo;
    private String taxNumber;
    private String sector;
    @ElementCollection
    private List<String> holidayDates;
    private String description;
    private String companyPhone;
    private String companyMail;
    private String wageDate;
    private Long subscriptionExpirationDate;

}
