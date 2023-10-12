package com.hrmanagement.controller;

import com.hrmanagement.dto.request.ChangeAdvanceStatusRequestDto;
import com.hrmanagement.dto.request.CreateAdvancePermissionRequestDto;
import com.hrmanagement.dto.response.CreateAdvancePermissionResponseDto;
import com.hrmanagement.dto.response.ListAdvanceResponseDto;
import com.hrmanagement.service.AdvancePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hrmanagement.constant.ApiUrls.ADVANCE;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(ADVANCE)
@RequiredArgsConstructor
public class AdvanceController {
    private final AdvancePermissionService service;

    @PostMapping("/createAdvance/{token}")
    public ResponseEntity<CreateAdvancePermissionResponseDto> createAdvance(@PathVariable String token, @RequestBody CreateAdvancePermissionRequestDto dto){
        return ResponseEntity.ok(service.createAdvance(token,dto));
    }

    @GetMapping("/getAllPendingAdvance/{token}")
    public ResponseEntity<List<ListAdvanceResponseDto>> getAllAdvance(@PathVariable String token){
        return ResponseEntity.ok(service.getAllPendingAdvanceByCompanyId(token));
    }

    @GetMapping("/getAllAdvance/{token}")
    public ResponseEntity<List<ListAdvanceResponseDto>> getAllAdvanceByCompanyId(@PathVariable String token){
        return ResponseEntity.ok(service.getAllAdvancesByCompanyId(token));
    }
    @PutMapping("/change-advance-status/{token}")
    public ResponseEntity<Boolean> changeAdvanceStatus(@PathVariable String token, @RequestBody ChangeAdvanceStatusRequestDto dto) {
        return ResponseEntity.ok(service.changeAdvanceStatus(token,dto));
    }
    @GetMapping("/getAllAdvances")
    public ResponseEntity<List<ListAdvanceResponseDto>> getAllAdvances() {
        return ResponseEntity.ok(service.getAllAdvance());
    }
}
