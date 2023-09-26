package com.hrmG3.rabbitmq.model;

import com.hrmG3.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterMailModel implements Serializable {

    private Long authId;
    private String name;
    private String surname;
    private String email;
    private String activationCode;
    private EStatus status;
}
