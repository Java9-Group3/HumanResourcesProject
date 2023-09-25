package com.hrmG3.repository.entity;

import com.hrmG3.repository.entity.enums.ERole;
import com.hrmG3.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class UserProfile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Anahtar alanı
    private String userId;
    private Long authId;
    private String password;
    private String email;
    private EStatus status;
    @ElementCollection(targetClass = ERole.class)
    @Enumerated(EnumType.STRING)
    @Builder.Default    //rollerin listesini tuttum
    private List<ERole> role = new ArrayList<>();

}
