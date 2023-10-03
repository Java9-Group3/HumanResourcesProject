package com.hrmanagement.controller;

import com.hrmanagement.dto.request.CompanyNameAndWageDateRequestDto;
import com.hrmanagement.dto.request.FindPendingCommentWithCompanyName;
import com.hrmanagement.dto.response.*;
import com.hrmanagement.dto.request.SaveCompanyRequestDto;
import com.hrmanagement.repository.entity.Company;
import com.hrmanagement.service.CompanyService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hrmanagement.constants.EndPoints.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(COMPANY)
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/save")
    @Operation(summary = "Şirket kaydeder.")
    public ResponseEntity<Boolean> saveCompanyRequestDto(@RequestBody SaveCompanyRequestDto dto){
        return ResponseEntity.ok(companyService.save(dto));
    }

    @GetMapping("/show-company-information")
    @Operation(summary = "Id'si sorgulanan şirketin bilgilerini gösterir.")
    public ResponseEntity<CompanyInformationResponseDto> showCompanyInformation(Long companyId){
        return ResponseEntity.ok(companyService.showCompanyInformation(companyId));
    }
    @GetMapping("/find-all-company-preview-information")
    @Operation(summary = "Bütün şirketlerin temel bilgilerini listeler.(Id,Logo,Şirket ismi,Posta Kodu,Şehir)")
    public ResponseEntity<List<VisitorCompanyInformations>> findAllCompanyPreviewInformation(){
        return ResponseEntity.ok(companyService.findAllCompanyPreviewInformation());
    }

    @GetMapping("/find-all-detailed-company-information/{companyId}")
    @Operation(summary = "Id'si sorgulanan şirketin bütün bilgilerini getirir.")
    public ResponseEntity<VisitorDetailedCompanyInformationResponse> findCompanyDetailedInformation(@PathVariable Long companyId){
        return ResponseEntity.ok(companyService.findCompanyDetailedInformation(companyId));
    }

    @GetMapping("/find-all")
    @Operation(summary = "Bütün şirketleri listeler.")
    public ResponseEntity<List<Company>> findAll(){
        return ResponseEntity.ok(companyService.findAll());
    }


    @GetMapping("/get-personnel-company-information/{companyId}")
    @Operation(summary = "Id'si sorgulanan şirketin logosu ve şirketin ismiyle birlikte personel izin günlerini getirir.")
    public ResponseEntity<PersonnelCompanyInformationResponseDto> getPersonnelCompanyInformation(@PathVariable Long companyId){
        return ResponseEntity.ok(companyService.getPersonnelCompanyInformation(companyId));
    }

    @GetMapping("/does-company-exists/{companyId}")
    @Operation(summary = "Id'si sorgulanan şirketin olup olmadığını kontrol eder.Geriye boolean döner.")
    public ResponseEntity<Boolean> doesCompanyExist(@PathVariable Long companyId){
        return ResponseEntity.ok(companyService.doesCompanyIdExist(companyId));
    }

    @GetMapping("/find-comments-with-company-name-by-status/{token}")
    public ResponseEntity<List<FindPendingCommentWithCompanyName>> findCommentWithCompanyNameByStatus(@PathVariable String token) {
        return ResponseEntity.ok(companyService.findCommentWithCompanyNameByStatus(token));
    }
    @Hidden
    @GetMapping("/get-company-name-with-company-id/{companyId}")
    public ResponseEntity<String> getCompanyNameWithCompanyId(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyService.getCompanyNameWithCompanyId(companyId));
    }
    @Hidden
    @GetMapping("get-personnel-dashboard-information/{token}")
    public ResponseEntity<PersonnelDashboardResponseDto>getPersonnelDashboardInformation(@PathVariable String token){
        return ResponseEntity.ok(companyService.getPersonnelDashboardInformation(token));
    }
    @Hidden
    @GetMapping("get-manager-dashboard-information/{token}")
    public ResponseEntity<ManagerDashboardResponseDto> getManagerDashboardInformation(@PathVariable String token){
        return ResponseEntity.ok(companyService.getManagerDashboardInformation(token));
    }
    @Hidden
    @GetMapping("/get-company-infos-with-company-id/{companyId}")
    public ResponseEntity<AllCompanyInfosForUserProfileResponseDto> getAllInfosCompanyWithCompanyId(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyService.getAllInfosCompanyWithCompanyId(companyId));
    }

    @Hidden
    @GetMapping("/get-company-name-and-wagedate-with-company-id/{companyId}")
    ResponseEntity<CompanyNameAndWageDateRequestDto> getCompanyNameAndWageDateResponseDto(@PathVariable Long companyId){
        return ResponseEntity.ok(companyService.getCompanyNameAndWageDateResponseDto(companyId));
    }
    @Hidden
    @PutMapping("/subscribe-company")
    public ResponseEntity<Boolean> subscribeCompany(@RequestBody SubscribeCompanyResponseDto dto){
        return ResponseEntity.ok(companyService.subscribeCompany(dto));
    }
    @Hidden
    @GetMapping("/does-company-subscription-exist/{companyId}")
    public ResponseEntity<Boolean> doesCompanySubscriptionExist(@PathVariable Long companyId){
        return ResponseEntity.ok(companyService.doesCompanySubscriptionExist(companyId));
    }
    @Hidden
    @PutMapping("/update-company-wage-date")
    ResponseEntity<Boolean> updateCompanyWageDate(@RequestBody UpdateCompanyWageDateResponseDto dto){
        return ResponseEntity.ok(companyService.updateCompanyWageDate(dto));
    }
    @GetMapping(FINDBYCOMPANYNAME)
    public ResponseEntity<Company> findCompanyByCompanyName(String companyName) {
        return ResponseEntity.ok(companyService.findCompanyByCompanyName(companyName));
    }


}
