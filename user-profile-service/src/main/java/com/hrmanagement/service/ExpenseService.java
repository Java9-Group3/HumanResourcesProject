package com.hrmanagement.service;

import com.hrmanagement.dto.request.CreateExpenseRequestDto;
import com.hrmanagement.dto.response.CreateExpenseResponseDto;
import com.hrmanagement.dto.response.ListExpenseResponseDto;
import com.hrmanagement.manager.ICompanyManager;
import com.hrmanagement.mapper.IExpenseMapper;
import com.hrmanagement.repository.IExpenseRepository;
import com.hrmanagement.repository.entity.Expense;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService extends ServiceManager<Expense,Long> {
    private final IExpenseRepository repository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ICompanyManager companyManager;


    public ExpenseService(IExpenseRepository repository, JwtTokenProvider jwtTokenProvider, ICompanyManager companyManager) {
        super(repository);
        this.repository = repository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.companyManager = companyManager;
    }
    //kendime not authid unutma
    public CreateExpenseResponseDto createExpense(String token,CreateExpenseRequestDto dto){

        Expense expense=IExpenseMapper.INSTANCE.toExpense(dto);

        expense.setAuthid(jwtTokenProvider.getAuthIdFromToken(token).get());
        String companyName =companyManager.getCompanyNameWithCompanyId(String.valueOf(expense.getAuthid())).getBody();
        expense.setCompanyName(companyName);

        save(expense);
        return new CreateExpenseResponseDto("Harcama talebi başarıyla eklendi");
    }

    //companyidsine göre talepleri getiriyor . koıntrol et ?
    public List<ListExpenseResponseDto> getAllExpensesByCompanyId(String token) {
        List<Expense> expenses = repository.findByCompanyId(jwtTokenProvider.getCompanyIdFromToken(token).get());
        List<ListExpenseResponseDto> dtoExpenses = new ArrayList<>();

        for (Expense expense : expenses) {
            ListExpenseResponseDto dto = IExpenseMapper.INSTANCE.toDto(expense);
            dtoExpenses.add(dto);
        }

        return dtoExpenses;
    }





}
