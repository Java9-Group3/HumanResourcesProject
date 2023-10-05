package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission,Long> {
    List<Permission> findAllByStatusAndCompanyId(String status, String companyId);
    List<Permission> findByUserIdAndStatus(String userId, String status);

}