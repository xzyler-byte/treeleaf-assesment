package com.xzyler.microservices.securityservice.dto.security.rolemenurightassignment;

import com.xzyler.microservices.securityservice.constants.FieldErrorConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleMenuRightAssignmentMasterDto {

    private Integer id;
    private Integer status;

    @Valid
    private List<RoleMenuRightAssignmentDetailDto> right;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date effectTillDate;

    @NotNull(message = FieldErrorConstants.NOT_NULL)
    @NotBlank(message = FieldErrorConstants.NOT_BLANK)
    private String effectDate;

    @NotNull(message = FieldErrorConstants.NOT_NULL)
    private Integer roleId;
}
