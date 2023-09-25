package com.hrmG3.manager;


import com.hrmG3.dto.request.NewCreateManagerUserRequestDto;
import com.hrmG3.dto.request.NewCreateVisitorUserRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(url = "http://localhost:9093/api/v1/user-profile", name = "auth-userprofile",decode404 = true)
public interface IUserProfileManager {

    /**
     * Yeni bir ziyaretçi kullanıcısı oluşturmak için uzak servisi çağırır.
     * @param dto Yeni ziyaretçi kullanıcı bilgilerini içeren DTO (Data Transfer Object).
     * @return Başarılı bir şekilde visitor oluşturulursa true, aksi takdirde false döner.
     */
    @PostMapping("/create-visitor")
    ResponseEntity<Boolean> createVisitorUser(@RequestBody NewCreateVisitorUserRequestDto dto);

    /**
     * Yeni bir yönetici kullanıcısı oluşturmak için uzak servisi çağırır.
     * @param dto Yeni yönetici kullanıcı bilgilerini içeren DTO (Data Transfer Object).
     * @return Başarılı bir şekilde manager oluşturulursa true, aksi takdirde false döner.
     */
    @PostMapping("/create-manager")
    public ResponseEntity<Boolean> createManagerUser(@RequestBody NewCreateManagerUserRequestDto dto);

}
