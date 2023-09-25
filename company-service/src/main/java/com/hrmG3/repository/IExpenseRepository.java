package com.hrmG3.repository;

import com.hrmG3.repository.entity.Expense;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExpenseRepository extends JpaRepository<Expense,Long> {
    @Query("SELECT e FROM Expense e WHERE e.companyId = :companyId")
    List<Expense> showExpensesForCompany(@Param("companyId") Long companyId);

}
