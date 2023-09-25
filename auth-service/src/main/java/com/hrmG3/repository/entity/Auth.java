package com.hrmG3.repository.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tblAuth")
public class Auth extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authId;
    @Column(unique = true)
    private String email;
    private String name;
    private String middleName;
    private String surname;
    private String password;
    private String activationCode;

    @ElementCollection(targetClass = ERole.class)
    @JoinTable(name = "tblRoleTypes", joinColumns = @JoinColumn(name = "authId"))
    @Column(name = "roleType", nullable = false)
    private List<ERole> roles;
    @Builder.Default
    private EStatus status = EStatus.PENDING;

}
