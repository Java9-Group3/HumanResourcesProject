package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IExpenseRepository extends JpaRepository<Expense,Long> {
    Optional<List<Expense>> findByAuthid(Long authid);

    Long findCompanyIdByAuthid(Long authid);
    List<Expense> findByCompanyId(Long companyId);

    @Query("SELECT p FROM Expense p WHERE p.approvalStatus = 'PENDING' AND p.companyName = :companyName")
    List<Expense> findByCompanyName(@Param("companyName") String companyName);

}
