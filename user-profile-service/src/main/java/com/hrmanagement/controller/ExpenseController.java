package com.hrmanagement.controller;

import com.hrmanagement.dto.request.CreateExpenseRequestDto;
import com.hrmanagement.dto.response.CreateExpenseResponseDto;
import com.hrmanagement.dto.response.ListExpenseResponseDto;
import com.hrmanagement.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hrmanagement.constant.ApiUrls.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(EXPENSE)
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService service;

    @PostMapping(SAVE+"/{token}")
    public ResponseEntity<CreateExpenseResponseDto> save(@PathVariable String token, @RequestBody CreateExpenseRequestDto dto){
        return ResponseEntity.ok(service.createExpense(token,dto));
    }

    @GetMapping(GETALLEXPENSES)
    public ResponseEntity<List<ListExpenseResponseDto>> getAllExpensesByCompanyId(String token) {
        return ResponseEntity.ok(service.getAllExpensesByCompanyId(token));
    }
}
