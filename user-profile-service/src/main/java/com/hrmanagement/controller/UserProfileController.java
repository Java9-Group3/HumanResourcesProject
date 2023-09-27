package com.hrmanagement.controller;

import com.hrmanagement.dto.request.*;
import com.hrmanagement.dto.response.*;
import com.hrmanagement.repository.entity.UserProfile;
import com.hrmanagement.service.UserProfileService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.hrmanagement.constant.ApiUrls.*;
import static com.hrmanagement.constant.ApiUrls.ADMINCHANGEVISITORSTATUS;

@RestController
@RequestMapping(USER_PROFILE)
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PutMapping(ADMINCHANGEVISITORSTATUS + "/{token}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Boolean> adminChangeManagerStatus(@PathVariable String token, @RequestBody ChangeManagerStatusRequestDto dto) {
        return ResponseEntity.ok(userProfileService.adminChangeManagerStatus(token, dto));
    }

    @PutMapping(FORGOT_PASSWORD)
    public ResponseEntity<Boolean> forgotPassword(@RequestBody ForgotPasswordUserResponseDto dto) {
        return ResponseEntity.ok(userProfileService.forgotPassword(dto));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(CREATE_PERSONAL + "/{token}")
    public ResponseEntity<Boolean> managerCreatePersonelUserProfile(@PathVariable String token, @RequestBody @Valid CreateUserProfileRequestDto dto) {

        return ResponseEntity.ok(userProfileService.managerCreatePersonelUserProfile(token, dto));
    }

    @PostMapping("/create-visitor")
    public ResponseEntity<Boolean> createVisitorUser(@RequestBody NewCreateVisitorUserResponseDto dto) {
        return ResponseEntity.ok(userProfileService.createVisitorUser(dto));
    }

    @PostMapping("/create-manager")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Boolean> createManagerUser(@RequestBody NewCreateManagerUserResponseDto dto) {
        return ResponseEntity.ok(userProfileService.createManagerUser(dto));
    }

    @Hidden
    @GetMapping("/get-manager-id/{authId}")
    public ResponseEntity<String> getCompanyId(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.getCompanyId(authId));
    }

    @GetMapping("/get-manager-names/{companyId}")
    public ResponseEntity<List<String>> getManagerNames(@PathVariable String companyId) {
        return ResponseEntity.ok(userProfileService.getManagerNames(companyId));
    }

    @PutMapping("/manager-delete-personnel/{token}/{userId}")
    public ResponseEntity<Boolean> managerDeletePersonnel(@PathVariable String token, @PathVariable Long userId) {
        return ResponseEntity.ok(userProfileService.managerDeletePersonnel(token, userId));
    }

    @GetMapping("/show-personnel-information/{token}")
    public ResponseEntity<PersonnelInformationResponseDto> showPersonnelInformation(@PathVariable String token) {
        return ResponseEntity.ok(userProfileService.showPersonnelInformation(token));
    }

    @PutMapping("/inactivate-user/{authId}")
    public ResponseEntity<Boolean> inactivateUser(@PathVariable Long authId) {
        System.out.println("burdayım");
        return ResponseEntity.ok(userProfileService.activateUser(authId));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<UserProfile>> findAll() {
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @Hidden
    @GetMapping("/get-userprofile-comment-information/{authId}")
    public ResponseEntity<UserProfileCommentResponseDto> getUserProfileCommentInformation(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.getUserProfileCommentInformation(authId));
    }

    @Hidden
    @GetMapping("/get-userprofile-expense-information/{authId}")
    public ResponseEntity<UserProfileExpenseResponseDto> getUserProfileExpenseInformation(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.getUserProfileExpenseInformation(authId));
    }

    @GetMapping("/find-all-manager-list/{token}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<FindAllManagerResponseDto>> findAllInactiveManager(@PathVariable String token) {
        return ResponseEntity.ok(userProfileService.findAllInactiveManager(token));
    }

    @Hidden
    @GetMapping("/get-userprofile-avatar-by-user-id/{userId}")
    public ResponseEntity<String> getUserAvatarByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userProfileService.getUserAvatarByUserId(userId));
    }



    @Hidden
    @GetMapping("/find-all-active-company-comments/{authId}")
    public ResponseEntity<PersonnelDashboardCommentRequestDto> findAllActiveCompanyComments(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.findAllActiveCompanyComments(authId));
    }




    @Hidden
    @GetMapping("/get-userprofile-manager-dashboard/{authId}")
    public ResponseEntity<UserProfileManagerDashboardRequestDto> getUserProfileManagerDashboard(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.getUserProfileManagerDashboard(authId));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/get-userprofile-avatar-and-name/{token}")
    public ResponseEntity<UserProfileAvatarAndNameResponseDto> getUserProfileAvatarAndName(@PathVariable String token) {
        return ResponseEntity.ok(userProfileService.getUserProfileAvatarAndName(token));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/get-userprofile-avatar-and-name-and-surname/{token}")
    public ResponseEntity<UserProfileAvatarAndNameAndSurnameResponseDto> getUserProfileAvatarAndNameAndSurname(@PathVariable String token) {
        return ResponseEntity.ok(userProfileService.getUserProfileAvatarAndNameAndSurname(token));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/updatePersonel")
    public ResponseEntity<Boolean> updatePersonel(@RequestBody PersonelUpdateRequestDto personelUpdateRequestDto) {
        return ResponseEntity.ok(userProfileService.updatePersonel(personelUpdateRequestDto));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/get-personel-profile-for-user-profile-dashboard/{token}")
    public ResponseEntity<UserProfileSendingInfosResponseDto> getPersonelProfileForUserProfileDashboard(@PathVariable String token) {
        return ResponseEntity.ok(userProfileService.getPersonelProfileForUserProfileDashboard(token));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/update-personel-address")
    public ResponseEntity<Boolean> updatePersonelAdress(@RequestBody PersonelAddressUpdateRequestDto personelUpdateRequestDto) {
        return ResponseEntity.ok(userProfileService.updatePersonelAdress(personelUpdateRequestDto));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping(PASS_CHANGE)
    public ResponseEntity<String> passwordChange(@RequestBody PasswordChangeDto dto) {
        return ResponseEntity.ok(userProfileService.passwordChange(dto));
    }

    @GetMapping("/does-founder-exists/{companyId}")
    public ResponseEntity<Boolean> doesFounderExists(@PathVariable String companyId) {
        return ResponseEntity.ok(userProfileService.doesFounderExists(companyId));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/get-wage/{token}")
    public ResponseEntity<Double> getWage(@PathVariable String token) {
        return ResponseEntity.ok(userProfileService.getWage(token));
    }


    @PostMapping("/founder-create-manager/{token}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Boolean> founderCreateManagerUserProfile(@PathVariable String token, @RequestBody @Valid CreateUserProfileRequestDto dto){
        return ResponseEntity.ok(userProfileService.founderCreateManagerUserProfile(token,dto));
    }

    @Hidden
    @GetMapping("/find-user-companyId/{authId}")
    public ResponseEntity<String> findUserCompanyId(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.getCompanyId(authId));
    }

    @Hidden
    @GetMapping("/find-avatar/{userId}")
    public ResponseEntity<String> findAvatar(@PathVariable Long userId){
        return ResponseEntity.ok(userProfileService.findAvatar(userId));
    }


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/update-avatar/{token}")
    public ResponseEntity<Boolean> updateAvatar(@PathVariable String token,@RequestBody UpdateAvatarRequestDto dto){
        return ResponseEntity.ok(userProfileService.updateAvatar(token,dto));
    }



}

