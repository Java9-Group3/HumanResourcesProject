package com.hrmG3.manager;

import com.hrmG3.dto.request.UpdateManagerStatusRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:9090/api/v1/auth", name = "userprofile-auth",decode404 = true)
public interface IAuthManager {

    /**
     * Yönetici durumunu güncellemek için uzak servisi çağırır.
     * @param dto Yönetici durumu güncelleme isteği DTO'su.
     * @return Başarılı bir şekilde güncellenirse true, aksi takdirde false döner.
     */
    @PutMapping("/update-manager-status")
    public ResponseEntity<Boolean> updateManagerStatus(@RequestBody UpdateManagerStatusRequestDto dto);
}
