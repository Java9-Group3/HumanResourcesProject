package com.hrmanagement.repository;


import com.hrmanagement.repository.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface IExpenseRepository extends JpaRepository<Expense,Long> {

    List<Expense> findAllByCompanyId(Long companyId);
    Optional<Expense> findByCompanyIdAndExpenseId(Long companyId, Long expenseId);
}
