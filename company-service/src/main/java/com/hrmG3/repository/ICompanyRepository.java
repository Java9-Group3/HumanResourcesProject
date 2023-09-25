package com.hrmG3.repository;

import com.hrmG3.repository.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompanyRepository extends JpaRepository<Company,Long> {

    boolean existsByCompanyNameIgnoreCase(String companyName);
}
