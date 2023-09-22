package com.hrmG3.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.hrmG3.repository.entity.enums.EStatus;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends BaseEntity {
    private String userId;
    private Long authId;
    private String password;
    private String email;
    private EStatus status;
    @Builder.Default    //rollerin listesini tuttum
    private List<ERole> role = new ArrayList<>();

}
