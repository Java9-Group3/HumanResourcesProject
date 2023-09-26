package com.hrmG3.controller;

import com.hrmG3.dto.request.LoginRequestDto;
import com.hrmG3.dto.request.RegisterManagerRequestDto;
import com.hrmG3.dto.request.RegisterVisitorRequestDto;
import com.hrmG3.dto.response.LoginResponseDto;
import com.hrmG3.dto.response.RegisterResponseDto;
import com.hrmG3.exception.AuthManagerException;
import com.hrmG3.exception.ErrorType;
import com.hrmG3.repository.entity.Auth;
import com.hrmG3.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.hrmG3.constant.EndPoints.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {
    private final AuthService authService;

    @PostMapping(REGISTERVISITOR)
    public ResponseEntity<Boolean> registerVisitor(@RequestBody @Valid RegisterVisitorRequestDto dto){
        return ResponseEntity.ok(authService.registerVisitor(dto));
    }

    @CrossOrigin(origins = {"*"})
    @PostMapping(REGISTERMANAGER)
    public ResponseEntity<RegisterResponseDto> registerManager(@RequestBody @Valid RegisterManagerRequestDto dto){
        if (!dto.getPassword().equals(dto.getRepassword())){
            throw new AuthManagerException(ErrorType.PASSWORD_DUPLICATE);
        }
        return ResponseEntity.ok(authService.registerManager(dto));
    }

    @CrossOrigin(origins = {"*"})
    @PostMapping(LOGIN)
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping(FINDALL)
    public ResponseEntity<List<Auth>> findAll(){
        return ResponseEntity.ok(authService.findAll());
    }

}