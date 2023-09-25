package com.hrmG3.controller;

import com.hrmG3.dto.request.AddCompanyRequestDto;
import com.hrmG3.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static com.hrmG3.constants.EndPoints.COMPANY;

@RestController
@RequestMapping(COMPANY)
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/addCompany")
    public ResponseEntity<Boolean> addCompanyRequestDto(String token,AddCompanyRequestDto addCompanyRequestDto) {
        return ResponseEntity.ok(companyService.addCompany(token,addCompanyRequestDto));
    }

//    @PostMapping("saveEmployeeToCompany")
//    public ResponseEntity<String> saveEmployeeDto(SaveEmployeeDto saveEmployeeDto) {
//        return ResponseEntity.ok(companyService.saveEmployeeDto);
//    }

    @GetMapping("showAllEmployee")
    public ResponseEntity<List<Object>> showAllEmployess() {
        return ResponseEntity.ok(Collections.singletonList(companyService.findAll()));
    }
}
