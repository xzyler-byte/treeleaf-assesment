package com.xzyler.microservices.securityservice.service.security.impl;

import com.xzyler.microservices.securityservice.entity.security.User;
import com.xzyler.microservices.securityservice.repository.security.UserRepository;
import com.xzyler.microservices.securityservice.service.security.VerifyService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;

@Service
public class VerifyServiceImpl implements VerifyService {

    private final String Secret = "test_secret";
    private final UserRepository userRepository;

    VerifyServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public String verify(String token) throws Exception {
        String passwordResetToken = null;
        String email = null;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(Secret))
                    .parseClaimsJws(token).getBody();

            passwordResetToken = String.valueOf(claims.get("id"));
            email = String.valueOf(claims.get("email"));
        }catch (Exception e) {
            throw new Exception("Token Expired");
        }
        User user = userRepository.findByUserNameOrEmail(email).orElseThrow(() -> new Exception("Bridged Payload"));
        if (!passwordResetToken.equals(user.getPasswordResetToken())){
            throw new Exception("link has already been used.");
        }
        return email;

    }
}
