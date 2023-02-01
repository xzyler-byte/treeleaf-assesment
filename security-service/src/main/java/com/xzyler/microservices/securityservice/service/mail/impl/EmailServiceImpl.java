package com.xzyler.microservices.securityservice.service.mail.impl;

import com.xzyler.microservices.securityservice.configuration.email.EmailHelper;
import com.xzyler.microservices.securityservice.dto.email.EmailCredentialDto;
import com.xzyler.microservices.securityservice.entity.security.User;
import com.xzyler.microservices.securityservice.entity.security.email.EmailCredentials;
import com.xzyler.microservices.securityservice.repository.email.EmailCredentialRepository;
import com.xzyler.microservices.securityservice.repository.security.UserRepository;
import com.xzyler.microservices.securityservice.service.mail.EmailService;
import com.xzyler.microservices.securityservice.util.NullAwareBeanUtilsBean;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender getJavaMailSender;

    private final EmailCredentialRepository emailCredRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ThreadPoolTaskExecutor taskExecutor;

    private final String Secret = "test_secret";

    @Autowired
    @Qualifier("emailConfigBean")
    private Configuration emailConfig;

    EmailServiceImpl(EmailHelper emailHelper,
                     EmailCredentialRepository emailCredRepo,
                     PasswordEncoder passwordEncoder,
                     UserRepository userRepository,
                     ThreadPoolTaskExecutor taskExecutor) throws Exception {
        this.getJavaMailSender = emailHelper.getJavaMailSender();
        this.emailCredRepo = emailCredRepo;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.taskExecutor = taskExecutor;
    }


    @Override
    public EmailCredentials getEmailCredentials() {
        return emailCredRepo.findOneByActive();
    }

    @Override
    public void update(EmailCredentialDto emailCredentialsPojo) {
        EmailCredentials emailCredentialDb = emailCredRepo.findOneByActive();
        BeanUtilsBean beanUtilsBean = new NullAwareBeanUtilsBean();
        try {
            // copy non null data
            beanUtilsBean.copyProperties(emailCredentialDb, EmailCredentials.builder()
                    .email(emailCredentialsPojo.getEmail())
                    .host(emailCredentialsPojo.getHost())
                    .password(emailCredentialsPojo.getPassword())
                    .port(emailCredentialsPojo.getPort())
                    .protocol(emailCredentialsPojo.getProtocol())
                    .date(new Date())
                    .build()
            ); // destination <- source
            emailCredRepo.save(emailCredentialDb);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Id doesn't Exists");
        }
    }


    @Override
    public void sendRegistrationEmail(User user, String password) {

        try {
            String jwt = getJwtToken(user, user.getPasswordResetToken());

            // Send Front End URL
            String url = "localhost:8088/#/auth/verify?token=" + jwt;

            Map<String, String> model = new HashMap<>();
            model.put("url", url);
            model.put("name", user.getFullName());
            model.put("username", user.getUsername());
            model.put("email", user.getEmail());
            MimeMessage message = getJavaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Template template = emailConfig.getTemplate("newAccount.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setText(html, true);
            mimeMessageHelper.setSubject("Security service System - Registration");

            taskExecutor.execute(new Thread() {
                public void run() {
                    getJavaMailSender.send(message);
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Async
    @Override
    public void sendResetEmail(User user) throws Exception {
        try {
            Map<String, String> model = new HashMap<>();
            UUID uuid = UUID.randomUUID();
            String jwt = getJwtToken(user, uuid.toString());

            String url = "localhost:8088/#/auth/reset_req?token=" + jwt;
            model.put("url", url);
            model.put("name", user.getFullName());
            model.put("username", user.getUsername());
            model.put("email", user.getEmail());

            MimeMessage message = getJavaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Template template = emailConfig.getTemplate("forgetPassword.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setText(html, true);
            mimeMessageHelper.setSubject("Security service System - Reset Password");

            taskExecutor.execute(new Thread() {
                public void run() {
                    getJavaMailSender.send(message);
                }
            });

            user.setPasswordResetToken(uuid.toString());
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Async
    @Override
    public void sendNewPasswordSetNotification(User user) throws Exception {
        try {
            Map<String, String> model = new HashMap<>();
            model.put("name", user.getFullName());
            model.put("username", user.getUsername());
            model.put("email", user.getEmail());

            MimeMessage message = getJavaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Template template = emailConfig.getTemplate("passwordChange.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setText(html, true);
            mimeMessageHelper.setSubject("Security service System - Password Changed");

            taskExecutor.execute(new Thread() {
                public void run() {
                    getJavaMailSender.send(message);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void sendRejectEmail(User user) throws Exception {
        try {
            Map<String, String> model = new HashMap<>();

            model.put("name", user.getFullName());
            model.put("username", user.getUsername());
            model.put("email", user.getEmail());

            MimeMessage message = getJavaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Template template = emailConfig.getTemplate("rejectUser.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setText(html, true);
            mimeMessageHelper.setSubject("Security service System - User Deactivate");

            taskExecutor.execute(new Thread() {
                public void run() {
                    getJavaMailSender.send(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }


    public String getJwtToken(User user, String uuid) {

        String jwt = Jwts.builder()
                .setClaims(new HashMap<String, Object>() {{
                    put("id", uuid);
                    put("email", user.getEmail());
                }})
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 4000000))
                .signWith(SignatureAlgorithm.HS256, Secret)
                .compact();
        return jwt;
    }

}
