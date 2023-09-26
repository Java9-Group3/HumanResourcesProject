package com.hrmG3.manager;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(url = "http://localhost:9090/api/v1/auth", name = "userprofile-auth",decode404 = true)
public interface IAuthManager {

}
