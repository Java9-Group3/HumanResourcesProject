package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.Auth;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.repository.entity.enums.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface IAuthRepository extends JpaRepository<Auth,Long> {
    Optional<Auth> findOptionalByEmail(String email);

    Optional<Auth> findOptionalByAuthId(Long authId);
    Optional<Auth> findOptionalByActivationCode(String activationCode);

    List<Auth> findByRolesContainsAndStatus(ERole role, EStatus status);


}
