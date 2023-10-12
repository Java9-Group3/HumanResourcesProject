package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.AdvancePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAdvancePermissionRepository extends JpaRepository<AdvancePermission,Long> {

    @Query("SELECT SUM(ap.amount) FROM AdvancePermission ap WHERE ap.authid = :authId")
    Optional<Double> findOptionalSumAmountByAuthid(Long authId);

    Optional<List<AdvancePermission>> findByCompanyId(Long companyId);
}
