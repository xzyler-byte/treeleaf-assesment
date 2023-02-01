package com.xzyler.microservices.blogservice.controller;

import com.xzyler.microservices.blogservice.proxy.UserServiceProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BlogController extends BaseController {

    private final UserServiceProxy userServiceProxy;

    public BlogController(UserServiceProxy userServiceProxy) {
        this.userServiceProxy = userServiceProxy;
    }

    @GetMapping("/callproxy")
//    @PreAuthorize("{hasPermission(#this.this.permissionName,'get')}")
    public ResponseEntity<?> callProxy() throws Exception {
        return userServiceProxy.getUsers();
    }
}
