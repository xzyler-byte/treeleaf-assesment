package com.xzyler.microservices.securityservice.dto.security.userroleassignment;

import com.xzyler.microservices.securityservice.constants.FieldErrorConstants;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleAssignmentDetailDto {

    @NotNull(message = FieldErrorConstants.NOT_NULL)
    private Integer roleId;
}
