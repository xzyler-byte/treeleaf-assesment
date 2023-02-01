package com.xzyler.microservices.securityservice.dto.security;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;

    @NotBlank(message = "UserName is required")
    private String username;

    private String password;

    @Email
    private String email;

    private Boolean emailConfirmed;

    private String mobileNo;

    private String fullName;

    private String address;

    private String PasswordResetToken;

    private Date lockOutEnd;

    private MultipartFile image;

    private Integer status;

    private Integer imageAction;

    private Integer gender;

}
