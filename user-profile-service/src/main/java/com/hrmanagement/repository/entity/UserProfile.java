package com.hrmanagement.repository.entity;

import com.hrmanagement.repository.entity.enums.EGender;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserProfile extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long authId;
    private String password;
    private String email;
    private String name;
    private String middleName;
    private String surname;
    private String birthPlace;
    private String identificationNumber;
    @ElementCollection(targetClass = ERole.class)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<ERole> role = new ArrayList<>();
    private EStatus status;
    private EGender gender;
    private String phone;
    private double wage;
    private String avatar;
    private String neighbourhood;
    private String district;
    private String province;
    private String country;
    private Integer buildingNumber;
    private Integer apartmentNumber;
    private Integer postalCode;
    private String companyId;
    private int employeeLeaves;
    private String department;
    private Long jobEndingDate;
    @ElementCollection(targetClass = ERole.class)
    private List<String> jobBreak;
    private String jobShift;
    private String jobStartingDate;
    private String dateOfBirth;



}
