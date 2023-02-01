package com.xzyler.microservices.securityservice.dto.security.rolemenurightassignment;

import com.xzyler.microservices.securityservice.constants.FieldErrorConstants;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleMenuRightAssignmentDetailDto {

    private Integer id;

    @NotNull(message = FieldErrorConstants.NOT_NULL)
    private Integer menuId;

    private Boolean isCreate;

    private Boolean isUpdate;

    private Boolean isDelete;

    private Boolean isView;

    private Boolean isApprove;

    private Boolean isReport;
}
