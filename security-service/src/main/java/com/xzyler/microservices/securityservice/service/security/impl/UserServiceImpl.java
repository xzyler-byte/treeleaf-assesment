package com.xzyler.microservices.securityservice.service.security.impl;

import com.xzyler.microservices.securityservice.configuration.security.DefaultPasswordEncoderFactories;
import com.xzyler.microservices.securityservice.dto.security.HelperDto;
import com.xzyler.microservices.securityservice.dto.security.UserDto;
import com.xzyler.microservices.securityservice.entity.security.ApplicationUserImage;
import com.xzyler.microservices.securityservice.entity.security.User;
import com.xzyler.microservices.securityservice.entity.security.UserPasswordHist;
import com.xzyler.microservices.securityservice.enums.Gender;
import com.xzyler.microservices.securityservice.enums.Status;
import com.xzyler.microservices.securityservice.repository.security.UserRepository;
import com.xzyler.microservices.securityservice.service.mail.EmailService;
import com.xzyler.microservices.securityservice.service.security.UserService;
import com.xzyler.microservices.securityservice.util.CustomMessageSource;
import com.xzyler.microservices.securityservice.util.MultipartFileHandler;
import com.xzyler.microservices.securityservice.util.RandomUtils;
import com.xzyler.microservices.securityservice.util.UserDataConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomMessageSource customMessageSource;
    private final EmailService emailService;
    private final UserDataConfig userDataConfig;
    private final String secret = "doi_secret";

    @Override
    public List<Map<String, Object>> fetchUsers() throws Exception {
        return userRepository.findAll(PageRequest.of(0, 100));
    }

    @Override
    public Map<String, Object> fetchUser(Integer user_Id) throws Exception {
        Map<String, Object> user = userRepository.findUserById(user_Id);
        return user;
    }

    @Override
    @Transactional
    public void createUser(UserDto userDto) throws Exception {
        MultipartFileHandler multiPartFileHandler = new MultipartFileHandler();
        Gender gender = Gender.getByKey(userDto.getGender());

//        String password = RandomUtils.generateRandomUuid();
        String passwordHash = DefaultPasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userDto.getPassword());

        //We can create a DTO to Entity converter for this.
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordHash)
                .email(userDto.getEmail())
                .mobileNo(userDto.getMobileNo())
                .gender(gender)
                .fullName(userDto.getFullName())
                .address(userDto.getAddress())
                .passwordResetToken(UUID.randomUUID().toString())
                .emailConfirmed(false)
                .applicationUserImages(Collections.singletonList(ApplicationUserImage.builder()
                        .image(multiPartFileHandler.handle(userDto.getImage()))
                        .build()))
                .build();

        user.setStatus(Status.ACTIVE);
        user = userRepository.saveAndFlush(user);

//        emailService.sendRegistrationEmail(user, password);

    }


    @Override
    @Transactional
    public void updateUser(UserDto userDto) {
        MultipartFileHandler multiPartFileHandler = new MultipartFileHandler();
        String passwordHash = null;
        if (userDto.getPassword() != null) {
            passwordHash = DefaultPasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userDto.getPassword());
        }
        try {
            User userDB = userRepository.findById(userDto.getId()).orElseThrow(() -> new Exception("User Not Found."));
            Gender gender = Gender.getByKey(userDto.getGender());

            userDB.setPassword(passwordHash == null ? userDB.getPassword() : passwordHash);
            userDB.setEmail(userDto.getEmail());
            userDB.setMobileNo(userDto.getMobileNo());
            userDB.setUsername(userDto.getUsername());
            userDB.setFullName(userDto.getFullName());
            userDB.setAddress(userDto.getAddress());
            userDB.setGender(gender);

            if (userDto.getImage() != null) {
                List<ApplicationUserImage> applicationUserImages = userDB.getApplicationUserImages();
                applicationUserImages.add(ApplicationUserImage.builder()
                        .image(multiPartFileHandler.handle(userDto.getImage())).build());
                userDB.setApplicationUserImages(applicationUserImages);
            }

            if (userDto.getImageAction().equals(3) && userDto.getImage() == null) {
                List<ApplicationUserImage> applicationUserImages = userDB.getApplicationUserImages();
                applicationUserImages.add(ApplicationUserImage.builder()
                        .image(null).build());
                userDB.setApplicationUserImages(applicationUserImages);
            }

            userRepository.save(userDB);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteUser(Integer user_id) throws Exception {
        User userDB = userRepository.findById(user_id).orElseThrow(() -> new Exception("User Not found"));
        userDB.setStatus(Status.INACTIVE);
        userRepository.save(userDB);
    }

    @Override
    public void resetReqPassword(HelperDto helperDto) throws Exception {
        User user = userRepository.findByEmail(helperDto.getEmail()).orElseThrow(() -> new Exception(customMessageSource.get("error.doesn't.exist",
                customMessageSource.get("user"))));

        UUID uuid = UUID.randomUUID();
        user.setPasswordResetToken(uuid.toString());
        user = userRepository.save(user);

        emailService.sendResetEmail(user);

    }

    @Override
    public void changePassword(HelperDto helperDto) throws Exception {

        User user = userRepository.findById(helperDto.getId()).orElseThrow(() -> new Exception("User Not Found"));
        if (userDataConfig.getLoggedInUserId().equals(user.getId())) {
            if (!DefaultPasswordEncoderFactories.createDelegatingPasswordEncoder().matches(helperDto.getOldPassword(), user.getPassword())) {
                throw new Exception(customMessageSource.get("error.old.password.mismatch"));
            }
        }
        String encodedPassword = DefaultPasswordEncoderFactories.createDelegatingPasswordEncoder().encode(helperDto.getPassword());
        user.setPassword(encodedPassword);

        //save password change history
        List<UserPasswordHist> userPasswordHists = user.getUserPasswordHists();
        userPasswordHists.add(UserPasswordHist.builder()
                .isSystemGenerated(false)
                .password(encodedPassword)
                .build());
        user.setUserPasswordHists(userPasswordHists);

        userRepository.save(user);
    }

    @Override
    public void resetPasswordProcess(HelperDto helperDto) throws Exception {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                    .parseClaimsJws(helperDto.getToken()).getBody();
            String passwordResetToken = String.valueOf(claims.get("id"));
            String email = String.valueOf(claims.get("email"));
            User user = userRepository.findByUserNameOrEmail(email).orElseThrow(() -> new Exception("Bridged Payload"));
            if (!passwordResetToken.equals(user.getPasswordResetToken())) {
                throw new Exception("link has already been used.");
            }
            user.setPassword(DefaultPasswordEncoderFactories.createDelegatingPasswordEncoder().encode(helperDto.getPassword()));
            user.setPasswordResetToken(null);
            //save password change history
            List<UserPasswordHist> userPasswordHists = user.getUserPasswordHists();
            userPasswordHists.add(UserPasswordHist.builder()
                    .isSystemGenerated(false)
                    .password(user.getPassword())
                    .build());
            user.setUserPasswordHists(userPasswordHists);
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Integer changeStatus(Integer id, Integer status) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception(customMessageSource.get("error.doesn't.exist", customMessageSource.get("user"))));
        user.setStatus(Status.getByKey(status));
        userRepository.save(user);
        return id;
    }

    @Override
    public List<Map<String, Object>> getUsersListByRole(Integer roleId) throws Exception {
        return userRepository.getListOfUsersByRole(roleId);
    }

    @Override
    public Map<String, Object> getUserProfile(Integer userId) throws Exception {
        Map<String, Object> userProfileData = new HashMap<>();
        Map<String, Object> userDetails = userRepository.findUserById(userId);
        if (userDetails.isEmpty()) {
            throw new Exception(customMessageSource.get("error.doesn't.exist", customMessageSource.get("user")));
        }
        userProfileData.put("userDetails", userDetails);
        return userProfileData;
    }


}
