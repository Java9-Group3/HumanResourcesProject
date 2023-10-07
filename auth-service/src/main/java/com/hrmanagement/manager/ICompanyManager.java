package com.hrmanagement.manager;

import com.hrmanagement.dto.request.ChangeCommentStatusRequestDto;
import com.hrmanagement.dto.request.SaveCompanyRequestDto;
import com.hrmanagement.dto.request.SubscribeCompanyRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "http://localhost:9091/api/v1/company", name = "auth-company",decode404 = true)
public interface ICompanyManager {
    @GetMapping("/does-company-exists/{companyId}")
    public ResponseEntity<Boolean> doesCompanyExist(@PathVariable Long companyId);

//    @GetMapping("/does-company-exists/{companyId}")
//    public ResponseEntity<Optional<Long>> doesCompanyExist(@PathVariable Long companyId);

    @PostMapping("/save")
    @Operation(summary = "Şirket kaydeder.")
    public ResponseEntity<Long> saveCompanyRequestDto(@RequestBody SaveCompanyRequestDto dto);

    @PutMapping("/update")
    @Operation(summary = "Şirket günceller.")
    public ResponseEntity<Long> updateCompanyRequestDto(@RequestBody SaveCompanyRequestDto dto);

    @PutMapping("/subscribe-company")
    public ResponseEntity<Boolean> subscribeCompany(@RequestBody SubscribeCompanyRequestDto subscribeCompanyRequestDto);

    @GetMapping("/does-company-subscription-exist/{companyId}")
    public ResponseEntity<Boolean> doesCompanySubscriptionExist(@PathVariable Long companyId);

    @PutMapping("/change-comment-status/{token}")
    public ResponseEntity<Boolean> adminChangeCommentStatus(@PathVariable String token, @RequestBody ChangeCommentStatusRequestDto dto);
}
