package com.hrmG3.manager;


import com.hrmG3.dto.request.NewCreateManagerUserRequestDto;
import com.hrmG3.dto.request.NewCreateVisitorUserRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(url = "http://localhost:9093/api/v1/user-profile", name = "auth-userprofile",decode404 = true)
public interface IUserProfileManager {

    @PostMapping("/create-visitor")
    public ResponseEntity<Boolean> createVisitorUser(@RequestBody NewCreateVisitorUserRequestDto dto);

    @PostMapping("/create-manager")
    public ResponseEntity<Boolean> createManagerUser(@RequestBody NewCreateManagerUserRequestDto dto);


    @PutMapping("/inactivate-user/{authId}")
    public ResponseEntity<Boolean> inactivateUser(@PathVariable Long authId);

    @GetMapping("/does-founder-exists/{companyId}")
    public ResponseEntity<Boolean> doesFounderExists(@PathVariable String companyId);

}
