package com.hrmG3.controller;

import com.hrmG3.dto.request.RegisterManagerRequestDto;
import com.hrmG3.dto.request.RegisterVisitorRequestDto;
import com.hrmG3.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.hrmG3.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {

    private final AuthService authService;

    @PostMapping(SAVE_VISITOR)
    public ResponseEntity<Boolean> registerVisitor(@RequestBody @Valid RegisterVisitorRequestDto dto){
        return ResponseEntity.ok(authService.registerVisitor(dto));
    }

    @PostMapping(SAVE_MANAGER)
    public ResponseEntity<Boolean> registerManager(@RequestBody @Valid RegisterManagerRequestDto dto){
        return ResponseEntity.ok(authService.registerManager(dto));
    }




}
