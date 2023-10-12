package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserProfileRequestDto {

//    @Email
    private String email;
    private String name;
//    private String middleName;
    private String password;
    private String surname;
//
//    private String birthPlace;
//
//    private String identificationNumber;
//    private EGender gender;
//
    private String phone;
//
    private Double wage;
//    private String base64Avatar;
//
//    private String neighbourhood;
//
//    private String district;
//
//    private String province;
//
//    private String country;
//
//    private Integer buildingNumber;
//
//    private Integer apartmentNumber;
//
//    private Integer postalCode;
//
//    private String department;
//
    private String jobShift;
    private String jobBreak;
//
//    private int employeeLeaves;
//    private String dateOfBirth;
//    private String jobStartingDate;

}
