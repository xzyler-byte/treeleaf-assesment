package com.xzyler.microservices.securityservice.service.mail;

import com.xzyler.microservices.securityservice.dto.email.EmailCredentialDto;
import com.xzyler.microservices.securityservice.entity.security.User;
import com.xzyler.microservices.securityservice.entity.security.email.EmailCredentials;

public interface EmailService {
    EmailCredentials getEmailCredentials();

    void update(EmailCredentialDto emailCredentials);

    void sendRegistrationEmail(User user, String password);

    void sendResetEmail(User user) throws Exception;

    void sendNewPasswordSetNotification(User user) throws Exception;

    void sendRejectEmail(User user) throws Exception;
}
