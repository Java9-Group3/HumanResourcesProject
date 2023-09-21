package com.hrmG3.repository.entity;

import com.hrmG3.repository.enums.EUserType;
import lombok.*;
import com.hrmG3.repository.enums.EStatus;

import javax.persistence.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tblAuth")
public class Auth extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private String activationCode;


    @Builder.Default
    @Enumerated(EnumType.STRING)
    EUserType userType = EUserType.USER;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    EStatus status = EStatus.PENDING;

    String taxNo;
    String companyName;


}
