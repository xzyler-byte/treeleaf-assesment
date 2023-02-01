package com.xzyler.microservices.securityservice.dto.security;

import com.xzyler.microservices.securityservice.constants.FieldErrorConstants;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuDto {

    private Integer id;

    @NotNull(message = FieldErrorConstants.NOT_NULL)
    @NotBlank(message = FieldErrorConstants.NOT_BLANK)
    private String menuName;

    private String menuIcon;

    private Integer masterMenuId;

    private String menuCode;

    @NotNull(message = FieldErrorConstants.NOT_NULL)
    private Integer order;

    private String url;

    private Boolean isHidden = false;

}
