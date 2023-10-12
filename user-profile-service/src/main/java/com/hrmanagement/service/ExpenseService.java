package com.hrmanagement.service;

import com.hrmanagement.dto.request.ChangeExpenseStatuesRequestDto;
import com.hrmanagement.dto.request.CreateExpenseRequestDto;
import com.hrmanagement.dto.response.CreateExpenseResponseDto;
import com.hrmanagement.dto.response.ListExpenseResponseDto;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.exception.UserProfileManagerException;
import com.hrmanagement.manager.ICompanyManager;
import com.hrmanagement.mapper.IExpenseMapper;
import com.hrmanagement.repository.IExpenseRepository;
import com.hrmanagement.repository.entity.Expense;
import com.hrmanagement.repository.entity.UserProfile;
import com.hrmanagement.repository.entity.enums.ApprovalStatus;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService extends ServiceManager<Expense, Long> {
    private final IExpenseRepository repository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ICompanyManager companyManager;

    private final UserProfileService userProfileService;

    public ExpenseService(IExpenseRepository repository, JwtTokenProvider jwtTokenProvider, ICompanyManager companyManager, UserProfileService userProfileService) {
        super(repository);
        this.repository = repository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.companyManager = companyManager;
        this.userProfileService = userProfileService;
    }

    //kendime not authid unutma
    public CreateExpenseResponseDto createExpense(String token, CreateExpenseRequestDto dto) {

        Expense expense = IExpenseMapper.INSTANCE.toExpense(dto);

        expense.setAuthid(jwtTokenProvider.getAuthIdFromToken(token).get());
        UserProfile userProfile=userProfileService.findByAuthId(expense.getAuthid());
        expense.setCompanyId(userProfile.getCompanyId());
        save(expense);
        return new CreateExpenseResponseDto("Harcama talebi başarıyla eklendi");
    }

    //companyidsine göre pending talepleri getiriyor . koıntrol et ?
    public List<ListExpenseResponseDto> getAllPendingExpensesByCompanyId(String token) {
        Optional<List<Expense>> expenses = repository.findByCompanyId(jwtTokenProvider.getCompanyIdFromToken(token).get());

        List<ListExpenseResponseDto> dtoExpenses = new ArrayList<>();

        for (Expense expense : expenses.get()) {
            if (expense.getApprovalStatus().equals(ApprovalStatus.PENDING)) {
                ListExpenseResponseDto dto = IExpenseMapper.INSTANCE.toDto(expense);
                dtoExpenses.add(dto);
            }
        }
        return dtoExpenses;
    }
    public List<ListExpenseResponseDto> getAllExpenses(){
        List<Expense> expenses = repository.findAll();
        List<ListExpenseResponseDto> dtoExpenses = new ArrayList<>();

        for (Expense expense : expenses) {
            ListExpenseResponseDto dto = IExpenseMapper.INSTANCE.toDto(expense);
            dtoExpenses.add(dto);
        }
        return dtoExpenses;
    }

    //tüm harcamaları getiriyor
    public List<ListExpenseResponseDto> getAllExpensesByCompanyId(String token) {
       Optional<List<Expense>> expenses = repository.findByCompanyId(jwtTokenProvider.getCompanyIdFromToken(token).get());
        List<ListExpenseResponseDto> dtoExpenses = new ArrayList<>();

        for (Expense expense : expenses.get()) {
            ListExpenseResponseDto dto = IExpenseMapper.INSTANCE.toDto(expense);
            dtoExpenses.add(dto);
        }
        return dtoExpenses;
    }

    //onaylama ve red durumu
    public Boolean changeExpenseStatus(String token, ChangeExpenseStatuesRequestDto dto) {
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (roles.isEmpty())
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);

        if (roles.contains(ERole.MANAGER.toString())) {
            Expense expenseid = findById(dto.getExpenseId()).orElseThrow(() -> {
                throw new UserProfileManagerException(ErrorType.EXPENSE_NOT_FOUND);
            });
            if (expenseid.getApprovalStatus() == ApprovalStatus.PENDING) {
                if(dto.getAction()){
                    expenseid.setApprovalStatus(ApprovalStatus.APPROVED);
                }else{
                    expenseid.setApprovalStatus(ApprovalStatus.REJECTED);
                }
                update(expenseid);
                return true;
            }
        }
        throw new UserProfileManagerException(ErrorType.NOT_MANAGER);
    }










}
