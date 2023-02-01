package com.xzyler.microservices.securityservice.dto.security;

import com.xzyler.microservices.securityservice.constants.FieldErrorConstants;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    private Integer id;

    @NotEmpty(message = "RoleName can not be empty")
    @NotNull(message = "RoleName is required")
    private String roleName;

    @NotNull(message = FieldErrorConstants.NOT_NULL)
    private Integer roleType;

}
