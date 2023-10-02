package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompanyRepository extends JpaRepository<Company,Long> {
    Boolean existsByCompanyNameIgnoreCase(String companyName);
    Boolean existsByCompanyId(Long companyId);

//    Optional<Company> findByCompanyName(String companyName);

}
