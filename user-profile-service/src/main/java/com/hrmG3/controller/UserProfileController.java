package com.hrmG3.controller;

import com.hrmG3.constant.EndPoints;
import com.hrmG3.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import com.hrmG3.dto.response.NewCreateVisitorUserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EndPoints.USER_PROFILE)
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;
    @PostMapping(EndPoints.SAVEVISITOR)
    public ResponseEntity<Boolean> createVisitorUser(@RequestBody NewCreateVisitorUserResponseDto dto) {
        return ResponseEntity.ok(userProfileService.createVisitorUser(dto));
    }


}
