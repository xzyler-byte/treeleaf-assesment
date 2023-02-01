//package com.xzyler.microservices.securityservice.controller.security;
//
//import com.xzyler.microservices.securityservice.controller.BaseController;
//import com.xzyler.microservices.securityservice.service.security.UserService;
//import com.xzyler.microservices.securityservice.service.security.VerifyService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * This controller handles the request and response regarding verification.
// * @since 2021
// * @version 1.0-2021
// * @for DOI
// */
//@RestController
//@RequestMapping("api/verify")
//@RequiredArgsConstructor
//public class VerificationController extends BaseController {
//    private final VerifyService verifyService;
//    private final UserService userService;
//
//    /**
//     * Function for verification check
//     * @param token
//     * @return
//     * @throws Exception
//     */
//    @GetMapping
//    public ResponseEntity<?> verify(@RequestParam("token") String token) throws Exception {
//        String email = verifyService.verify(token);
//        if (email != null) {
//            userService.approve(email);
//            return ResponseEntity.ok(successResponse("valid", true));
//        }else{
//            return ResponseEntity.ok(successResponse("Invalid", false));
//        }
//    }
//}
//
