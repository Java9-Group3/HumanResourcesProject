package com.hrmanagement.controller;

import com.hrmanagement.dto.request.ChangeExpenseStatusRequestDto;
import com.hrmanagement.dto.request.PersonelExpenseRequestDto;
import com.hrmanagement.dto.response.CompanyExpenseListResponseDto;
import com.hrmanagement.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hrmanagement.constants.EndPoints.EXPENSE;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(EXPENSE)
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/personnel-make-expense")
    public ResponseEntity<Boolean> personnelMakeExpense(@PathVariable String token, @RequestBody PersonelExpenseRequestDto dto) {
        return ResponseEntity.ok(expenseService.personnelMakeExpense(token, dto));
    }

    @GetMapping("/find-all-company-expense-list")
    public ResponseEntity<List<CompanyExpenseListResponseDto>> findAllCompanyExpenseList(@PathVariable String token) {
        return ResponseEntity.ok(expenseService.findAllCompanyExpenseList(token));
    }

    @PutMapping("/change-expense-status")
    public ResponseEntity<Boolean> changeExpenseStatus(@PathVariable String token, @RequestBody ChangeExpenseStatusRequestDto dto) {
        return ResponseEntity.ok(expenseService.changeExpenseStatus(token, dto));
    }

}
