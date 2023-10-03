package com.hrmanagement.manager;

import com.hrmanagement.dto.request.PersonnelCommentRequestDto;
import com.hrmanagement.dto.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(url = "http://localhost:9093/api/v1/user-profile", name = "company-userprofile",decode404 = true)
public interface IUserManager {

    @PostMapping("/personnel-make-comment/{token}")
    public ResponseEntity<Boolean> personnelMakeComment(@PathVariable String token, @RequestBody PersonnelCommentRequestDto dto);

    @GetMapping("/get-manager-id/{authId}")
    public ResponseEntity<Long> getCompanyId(@PathVariable Long authId);

    @GetMapping("/get-manager-names/{companyId}")
    public ResponseEntity<List<Long>> getManagerNames(@PathVariable Long companyId);

    @GetMapping("/get-userprofile-comment-information/{authId}")
    public ResponseEntity<UserProfileCommentResponseDto> getUserProfileCommentInformation(@PathVariable Long authId);

    @GetMapping("/get-userprofile-expense-information/{authId}")
    public ResponseEntity<UserProfileExpenseResponseDto> getUserProfileExpenseInformation(@PathVariable Long authId);
    @GetMapping("/get-userprofile-avatar-by-user-id/{userId}")
    ResponseEntity<Long> getUserAvatarByUserId(@PathVariable Long userId);

    @GetMapping("/get-userprofile-personnel-dashboard-information/{authId}")
    ResponseEntity<UserProfilePersonnelDashboardResponseDto> getUserProfilePersonnelDashboardInformation(@PathVariable Long authId);

    @GetMapping("/find-all-active-company-comments/{authId}")
    ResponseEntity<PersonnelDashboardCommentResponseDto> findAllActiveCompanyComments(@PathVariable Long authId);

    @GetMapping("/get-userprofile-manager-dashboard/{authId}")
    ResponseEntity<UserProfileManagerDashboardResponseDto> getUserProfileManagerDashboard(@PathVariable Long authId);

    @GetMapping("/find-user-companyId/{authId}")
    ResponseEntity<Long> findUserCompanyId(@PathVariable Long authId);

    @GetMapping("/find-avatar/{userId}")
    ResponseEntity<Long> findAvatar(@PathVariable String userId);
}
