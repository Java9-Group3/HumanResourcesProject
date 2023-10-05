package com.hrmanagement.service;

import com.hrmanagement.repository.IDeptRepository;
import com.hrmanagement.repository.entity.Debt;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DebtService extends ServiceManager<Debt,Long> {
    private final IDeptRepository deptRepository;

    public DebtService(IDeptRepository deptRepository) {
        super(deptRepository);
        this.deptRepository = deptRepository;
    }

    public Double getPersonnelDept(Long personelId) {
        Optional<Debt> debtOptional = deptRepository.findById(personelId);
        return debtOptional.map(Debt::getDebt).orElse(null);
    }
}
