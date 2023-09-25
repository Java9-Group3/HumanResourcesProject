package com.hrmG3.controller;

import com.hrmG3.constant.EndPoints;
import com.hrmG3.dto.response.NewCreateManagerUserResponseDto;
import com.hrmG3.dto.response.NewCreateVisitorUserResponseDto;
import com.hrmG3.repository.entity.UserProfile;
import com.hrmG3.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(EndPoints.USER_PROFILE)
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    /**
     * Yeni bir ziyaretçi kullanıcısı oluşturur.
     * @param dto Yeni kullanıcı bilgilerini içeren DTO (Data Transfer Object).
     * @return Başarılı bir şekilde kullanıcı oluşturulursa true, aksi takdirde false döner.
     */
    @PostMapping(EndPoints.SAVEVISITOR)
    public ResponseEntity<Boolean> createVisitorUser(@RequestBody NewCreateVisitorUserResponseDto dto) {
        return ResponseEntity.ok(userProfileService.createVisitorUser(dto));
    }

    /**
     * Tüm kullanıcı profillerini getirir.
     * @return Kullanıcı profilleri listesi.
     */
    @PostMapping(EndPoints.FINDALL)
    public ResponseEntity<List<UserProfile>> findall(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

    /**
     * Yeni bir yönetici kullanıcısı oluşturur.
     * @param dto Yeni kullanıcı bilgilerini içeren DTO (Data Transfer Object).
     * @return Başarılı bir şekilde kullanıcı oluşturulursa true, aksi takdirde false döner.
     */
    @PostMapping(EndPoints.SAVEMANAGER)
    public ResponseEntity<Boolean> createManagerUser(@RequestBody NewCreateManagerUserResponseDto dto){
        return ResponseEntity.ok(userProfileService.createManagerUser(dto));
    }

}
