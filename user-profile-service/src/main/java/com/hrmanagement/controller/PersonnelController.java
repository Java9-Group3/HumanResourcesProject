package com.hrmanagement.controller;

import com.hrmanagement.dto.request.ActionDayOffPermissionDto;
import com.hrmanagement.dto.request.TakeDayOffPermissionRequestDto;
import com.hrmanagement.dto.response.FindAllPendingDayOfPermissionResponseDto;
import com.hrmanagement.repository.entity.Permission;
import com.hrmanagement.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("api/v1/day-off-permission")
@RequiredArgsConstructor
public class PersonnelController {
    private final PermissionService permissionService;

    @PostMapping("/take-day-off-permission/{token}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Permission> takeDayOffPermission(@PathVariable String token, @RequestBody TakeDayOffPermissionRequestDto dto) throws ParseException {
        return ResponseEntity.ok(permissionService.takeDayOffPermission(token,dto));
    }

    @PutMapping("/action-day-off-permission/{token}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Boolean> actionDayOffPermission(@PathVariable String token,@RequestBody ActionDayOffPermissionDto dto) {
        return ResponseEntity.ok(permissionService.actionDayOffPermission(token, dto));
    }

    @GetMapping("/find-all-pending-day-off-permissions/{token}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<FindAllPendingDayOfPermissionResponseDto>> findAllPendingDayOffPermission(@PathVariable String token) {
        return ResponseEntity.ok(permissionService.findAllPendingDayOffPermission(token));
    }

//    @GetMapping("/get-permission/{personelId}")
//    @CrossOrigin(origins = "*", allowedHeaders = "*")
//    public ResponseEntity<Permission> getBreakAndShiftInformation(@PathVariable Long personelId) {
//        Permission permission = permissionService.getBreakAndShiftInformation(personelId);
//        if (permission != null) {
//            return ResponseEntity.ok(permission);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }

//    @GetMapping("/get-personnel-debt/{personelId}")
//    @CrossOrigin(origins = "*", allowedHeaders = "*")
//    public ResponseEntity<Double> getPersonnelDebt(@PathVariable Long personelId) {
//        Double debt = debtService.getPersonnelDebt(personelId);
//        if (debt != null) {
//            return ResponseEntity.ok(debt);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
}