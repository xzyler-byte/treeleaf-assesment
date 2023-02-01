package com.xzyler.microservices.securityservice.configuration.email;

import com.xzyler.microservices.securityservice.entity.security.email.EmailCredentials;
import com.xzyler.microservices.securityservice.repository.email.EmailCredentialRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailHelper {

    private final EmailCredentialRepository emailCredRepo;

    public EmailHelper(EmailCredentialRepository emailCredRepo) {
        this.emailCredRepo = emailCredRepo;
    }

    public JavaMailSender getJavaMailSender() throws Exception {
        try {
            EmailCredentials emailCredentials = emailCredRepo.findOneByActive();
            if (emailCredentials != null) {
                JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

                Properties props = new Properties();
                props.put("mail.transport.protocol", emailCredentials.getProtocol());
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.debug", "true");
                mailSender.setJavaMailProperties(props);

                mailSender.setHost(emailCredentials.getHost());
                mailSender.setPort(Integer.valueOf(emailCredentials.getPort()));
                mailSender.setUsername(emailCredentials.getEmail());
                mailSender.setPassword(emailCredentials.getPassword());
                return mailSender;
            } else {
                return new JavaMailSenderImpl();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
