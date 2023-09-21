package com.hrmG3.manager;


import com.hrmG3.dto.request.NewCreateVisitorUserRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(url = "http://localhost:9091/api/v1/user-profile", name = "auth-userprofile")
public interface IUserProfileManager {
    @PostMapping("/create-visitor")
    public ResponseEntity<Boolean> createVisitorUser(@RequestBody NewCreateVisitorUserRequestDto dto);

}
