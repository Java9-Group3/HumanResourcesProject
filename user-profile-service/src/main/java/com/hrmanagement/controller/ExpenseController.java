package com.hrmanagement.controller;

import com.hrmanagement.dto.request.ChangeExpenseStatuesRequestDto;
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
    //isterler özet;
    //bi save için site olacak. DONE
    //bekleyen istekler için sayfa olacak bu sayfa da onay ve red olmalı
    //tüm istekleri görüntüleyen bir sayfa olacak

    @PostMapping(SAVE+"/{token}")
    public ResponseEntity<CreateExpenseResponseDto> save(@PathVariable String token, @RequestBody CreateExpenseRequestDto dto){
        return ResponseEntity.ok(service.createExpense(token,dto));
    }

    @GetMapping("/getallpendingexpenses/{token}")
    public ResponseEntity<List<ListExpenseResponseDto>> getAllPendingExpensesByCompanyId(@PathVariable String token) {
        return ResponseEntity.ok(service.getAllPendingExpensesByCompanyId(token));
    }

    @GetMapping("/getallexpenses/{token}")
    public ResponseEntity<List<ListExpenseResponseDto>> getAllExpensesByCompanyId(@PathVariable String token) {
        return ResponseEntity.ok(service.getAllExpensesByCompanyId(token));
    }

    @PutMapping("/change-expense-status/{token}")
    public ResponseEntity<Boolean> changeExpenseStatus(@PathVariable String token, @RequestBody ChangeExpenseStatuesRequestDto dto) {
        return ResponseEntity.ok(service.changeExpenseStatus(token,dto));
    }

    @GetMapping("/getAllExpenses")
    public ResponseEntity<List<ListExpenseResponseDto>> getAllExpenses() {
        return ResponseEntity.ok(service.getAllExpenses());
    }



}
