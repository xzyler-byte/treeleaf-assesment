package com.xzyler.microservices.securityservice.dto.security.userroleassignment;

import com.xzyler.microservices.securityservice.constants.FieldErrorConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleAssignmentMasterDto {

    private Integer id;
    private Integer status;
    @NotNull(message = FieldErrorConstants.NOT_NULL)
    private Integer userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = FieldErrorConstants.NOT_NULL)
    private Date effectDate;

    private String effectTillDate;

    @Valid
    private List<UserRoleAssignmentDetailDto> right;
}
