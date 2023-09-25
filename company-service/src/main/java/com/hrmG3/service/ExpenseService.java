package com.hrmG3.service;

import com.hrmG3.dto.request.SaveExpenseRequestDto;
import com.hrmG3.repository.IExpenseRepository;
import com.hrmG3.repository.entity.Expense;
import com.hrmG3.utility.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService extends ServiceManager<Expense,Long> {
    private final IExpenseRepository expenseRepository;

    public ExpenseService(IExpenseRepository expenseRepository) {
        super(expenseRepository);
        this.expenseRepository = expenseRepository;
    }

    public ResponseEntity<?> saveExpense(SaveExpenseRequestDto saveExpenseRequestDto) {
        return null;
    }

    public Integer findCompanyExpenses() {
        return null;
    }

    public List<Expense> showExpesesForCompany(Long companyId) {
        return expenseRepository.showExpensesForCompany(companyId);
    }
}
