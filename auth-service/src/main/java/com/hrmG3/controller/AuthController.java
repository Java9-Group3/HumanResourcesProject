package com.hrmG3.controller;

import lombok.RequiredArgsConstructor;
import com.hrmG3.dto.request.RegisterVisitorRequestDto;
import com.hrmG3.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import static com.hrmG3.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {

    private final AuthService authService;

    @PostMapping(SAVE)
    public ResponseEntity<Boolean> registerVisitor(@RequestBody @Valid RegisterVisitorRequestDto dto){
        return ResponseEntity.ok(authService.registerVisitor(dto));
    }


}
