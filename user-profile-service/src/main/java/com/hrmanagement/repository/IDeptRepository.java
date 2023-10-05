package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDeptRepository extends JpaRepository<Debt,Long> {

}
