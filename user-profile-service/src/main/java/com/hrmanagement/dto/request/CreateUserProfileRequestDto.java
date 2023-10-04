package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserProfileRequestDto {

    @Email
    private String email;
    private String name;
//    private String middleName;
    private String surname;
    private String password;
//    @NotBlank
//    private String birthPlace;
//    @NotBlank
//    private String identificationNumber;
//    private EGender gender;
//    @NotBlank
//    private String phone;
//    @NotNull
//    private Double wage;
    private String base64Avatar;
//    @NotBlank
//    private String neighbourhood;
//    @NotBlank
//    private String district;
//    @NotBlank
//    private String province;
//    @NotBlank
//    private String country;
//    @NotNull
//    private Integer buildingNumber;
//    @NotNull
//    private Integer apartmentNumber;
//    @NotNull
//    private Integer postalCode;
//    @NotBlank
//    private String department;
//    @NotBlank
//    private String jobShift;
//    private List<String> jobBreak;
//    @NotNull
//    private int employeeLeaves;
//    private String dateOfBirth;
//    private String jobStartingDate;

}
