package com.hrmG3.controller;

import com.hrmG3.dto.response.NewCreateManagerUserResponseDto;
import com.hrmG3.dto.response.NewCreateVisitorUserResponseDto;
import com.hrmG3.repository.entity.UserProfile;
import com.hrmG3.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hrmG3.constant.EndPoints.USER_PROFILE;

@RestController
@RequestMapping(USER_PROFILE)
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/create-visitor")
    public ResponseEntity<Boolean> createVisitorUser(@RequestBody NewCreateVisitorUserResponseDto dto) {
        return ResponseEntity.ok(userProfileService.createVisitorUser(dto));
    }

    @PostMapping("/create-manager")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Boolean> createManagerUser(@RequestBody NewCreateManagerUserResponseDto dto) {
        return ResponseEntity.ok(userProfileService.createManagerUser(dto));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<UserProfile>> findAll() {
        return ResponseEntity.ok(userProfileService.findAll());
    }

}
