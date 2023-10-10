package com.hrmanagement.controller;

import com.hrmanagement.dto.request.*;

import com.hrmanagement.dto.response.*;
import com.hrmanagement.repository.entity.Auth;
import com.hrmanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.hrmanagement.constant.ApiUrls.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {
    private final AuthService authService;

    @GetMapping("/pending-managers")
    public ResponseEntity<List<PendingManagerResponseDtoList>> getPendingManagers() {
        List<PendingManagerResponseDtoList> pendingManagers = authService.findPendingManagers();
        return ResponseEntity.ok(pendingManagers);
    }
    @PostMapping(REGISTER_VISITOR)
    public ResponseEntity<Boolean> registerVisitor(@RequestBody @Valid RegisterVisitorRequestDto dto){
        return ResponseEntity.ok(authService.registerVisitor(dto));
    }

    @PostMapping(REGISTER_MANAGER)
    public ResponseEntity<RegisterResponseDto> registerManager(@RequestBody @Valid RegisterManagerRequestDto dto){
        return ResponseEntity.ok(authService.registerManager(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @Hidden
    @GetMapping(FORGOT_PASSWORD_REQUEST + "/{email}")
    public ResponseEntity<Boolean> forgotPasswordRequest(@PathVariable String email){
        return ResponseEntity.ok(authService.forgotPasswordRequest(email));
    }


    @GetMapping("/confirm-account")
    public ResponseEntity<ConfirmationResponseDto> confirmUserAccount(@RequestParam("activationCode") String activationCode) {
        ConfirmationResponseDto responseDto = authService.confirmUserAccount(activationCode);
        return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
    }

    @GetMapping(FIND_ALL)
    public ResponseEntity<List<Auth>> findAll(){
        return ResponseEntity.ok(authService.findAll());
    }

    @Hidden
    @PostMapping("/manager-create-personnel-userProfile")
    public ResponseEntity<Long> managerCreatePersonnelUserProfile(@RequestBody AuthCreatePersonnelProfileResponseDto dto){
        return ResponseEntity.ok(authService.managerCreatePersonelUserProfile(dto));
    }

    @Hidden
    @PostMapping(FORGOT_PASSWORD + "/{token}")
    public ResponseEntity<Boolean> forgotPassword(@PathVariable String token, @RequestBody @Valid ForgotPasswordChangePasswordRequestDto dto) {
        return ResponseEntity.ok(authService.forgotPassword(token,dto));
    }

    @PutMapping("/update-manager-status")
    public ResponseEntity<Boolean> updateManagerStatus(@RequestBody UpdateManagerStatusResponseDto dto){
        return ResponseEntity.ok(authService.updateManagerStatus(dto));
    }

    @Hidden
    @PutMapping("/manager-delete-personnel")
    public ResponseEntity<Boolean> managerDeletePersonnel(@RequestBody DeletePersonnelFromAuthResponseDto dto){
        return ResponseEntity.ok(authService.managerDeletePersonnel(dto));
    }
    @Hidden

    @GetMapping("/get-roles-from-token/{token}")
    public ResponseEntity<List<String>> getRolesFromToken(@PathVariable String token){
        return ResponseEntity.ok(authService.getRolesFromToken(token));
    }
    @Hidden
    @PutMapping("/update-userprofile-to-auth")
    public ResponseEntity<Boolean> updatePersonel(@RequestBody PersonelUpdateUserProfileToAuthRequestDto dto){
        return ResponseEntity.ok(authService.updateBecauseOfUserProfile(dto));
    }
    @Hidden
    @PutMapping("/password-change")
    public ResponseEntity<Boolean> passwordChange(@RequestBody ToAuthPasswordChangeDto dto){
        return ResponseEntity.ok(authService.passwordChange(dto));
    }

    @Hidden
    @PostMapping("/founder-create-manager-userProfile")
    public ResponseEntity<Long> founderCreateManagerUserProfile(@RequestBody AuthCreatePersonnelProfileResponseDto dto){
        return ResponseEntity.ok(authService.founderCreateManagerUserProfile(dto));
    }
    @GetMapping("/getPersonelList")
    public ResponseEntity<List<PersonelListResponseDto>> getPersonelList(String token){
        return ResponseEntity.ok(authService.getPersonelList(token));
    }
    @GetMapping("/getPersonelList2")
    public ResponseEntity<List<PersonelListResponseDto>> getPersonelList2(){
        return ResponseEntity.ok(authService.getPersonelList2());
    }
}
