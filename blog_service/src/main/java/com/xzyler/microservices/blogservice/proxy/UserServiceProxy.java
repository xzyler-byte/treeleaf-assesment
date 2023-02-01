package com.xzyler.microservices.blogservice.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "netflix-zuul-api-gateway-server/security-service")
@RibbonClient(name = "security-service")
public interface UserServiceProxy {

    @GetMapping("/api/user/get_all")
    ResponseEntity<?> getUsers() throws Exception;
}
