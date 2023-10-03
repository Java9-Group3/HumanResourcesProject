package com.hrmanagement.manager;

import com.hrmanagement.dto.request.PersonnelCommentRequestDto;
import com.hrmanagement.dto.request.UpdateCompanyWageDateRequestDto;
import com.hrmanagement.dto.response.AllCompanyInfosForUserProfileResponseDto;
import com.hrmanagement.dto.response.CompanyNameAndWageDateResponseDto;
import com.hrmanagement.dto.response.PersonnelCompanyInformationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "http://localhost:9091/api/v1/company", name = "userprofile-company",decode404 = true)
public interface ICompanyManager {

    @PostMapping("/personnel-make-comment/{token}")
    public ResponseEntity<Boolean> personnelMakeComment(@PathVariable String token, @RequestBody PersonnelCommentRequestDto dto);
    @GetMapping("/get-personnel-company-information/{companyId}")
    public ResponseEntity<PersonnelCompanyInformationResponseDto> getPersonnelCompanyInformation(@PathVariable Long companyId);
    @GetMapping("/get-company-name-with-company-id/{companyId}")
    ResponseEntity<String> getCompanyNameWithCompanyId(@PathVariable String companyId);
    @GetMapping("/get-company-infos-with-company-id/{companyId}")
    public ResponseEntity<AllCompanyInfosForUserProfileResponseDto> getAllInfosCompanyWithCompanyId(@PathVariable Long companyId);

    @GetMapping("/get-company-name-and-wagedate-with-company-id/{companyId}")
    ResponseEntity<CompanyNameAndWageDateResponseDto> getCompanyNameAndWageDateResponseDto(@PathVariable Long companyId);

    @PutMapping("/update-company-wage-date")
    ResponseEntity<Boolean> updateCompanyWageDate(@RequestBody UpdateCompanyWageDateRequestDto dto);
}
