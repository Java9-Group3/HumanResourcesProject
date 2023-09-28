package com.hrmanagement.repository;

import com.hrmanagement.dto.response.FindAllManagerResponseDto;
import com.hrmanagement.repository.entity.UserProfile;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.repository.entity.enums.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByAuthId(Long authId);

    List<UserProfile> findByCompanyId(Long companyId);
    Optional<UserProfile> findByEmail(String email);
    List<FindAllManagerResponseDto> findAllByStatusAndRole(EStatus status, ERole role);
    Integer countByCompanyId(Long companyId);
}
