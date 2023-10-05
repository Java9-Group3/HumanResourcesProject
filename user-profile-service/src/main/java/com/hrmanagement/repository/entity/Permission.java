package com.hrmanagement.repository.entity;

import com.hrmanagement.repository.entity.enums.EPermissionTypes;
import com.hrmanagement.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Permission extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;
    private Long userId;
    private Long companyId;
    private String startingDate;
    private String endingDate;
    @ElementCollection
    private List<String> permissionDates = new ArrayList<>();
    private String description;
    private EPermissionTypes ePermissionTypes;
    @Builder.Default
    private EStatus status = EStatus.PENDING;
}