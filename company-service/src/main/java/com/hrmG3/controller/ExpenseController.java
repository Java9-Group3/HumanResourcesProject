package com.hrmG3.controller;

import com.hrmG3.dto.request.SaveExpenseRequestDto;
import com.hrmG3.repository.entity.Expense;
import com.hrmG3.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hrmG3.constants.EndPoints.EXPENSE;

@RestController
@RequestMapping(EXPENSE)
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;
    @PostMapping("/createExpenses")
    public ResponseEntity<?> createExpense(SaveExpenseRequestDto saveExpenseRequestDto){
        return ResponseEntity.ok(expenseService.saveExpense(saveExpenseRequestDto));
    }
    @GetMapping("/showAllExpeses")
    public ResponseEntity<Integer> showAllExpeses() {
        return ResponseEntity.ok(expenseService.findCompanyExpenses());
    }
    @GetMapping("/showExpesesForCompany")
    public ResponseEntity<List<Expense>> showExpesesForCompany(Long companyId) {
        return ResponseEntity.ok(expenseService.showExpesesForCompany(companyId));
    }
}
