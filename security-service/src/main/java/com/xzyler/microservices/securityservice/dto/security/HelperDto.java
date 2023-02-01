package com.xzyler.microservices.securityservice.dto.security;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperDto {

    private Integer id;
    private String email;
    private String password;
    private String oldPassword;
    private String userName;
    private String token;
    private Integer status;
}
