package com.hrmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;

@AllArgsConstructor
@SuperBuilder
@Data
@NoArgsConstructor
@MappedSuperclass
public class Base {
    private Long createdDate;
    private Long updatedDate;
}
