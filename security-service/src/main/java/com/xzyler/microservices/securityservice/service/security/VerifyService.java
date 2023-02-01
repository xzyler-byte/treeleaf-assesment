package com.xzyler.microservices.securityservice.service.security;

public interface VerifyService {
    String verify(String token) throws Exception;
}
