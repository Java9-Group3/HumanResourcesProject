package com.hrmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Debt extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long debtId;
    private Long userId;
    private double debt;
    @Builder.Default
    private Long debtDate = System.currentTimeMillis();

}