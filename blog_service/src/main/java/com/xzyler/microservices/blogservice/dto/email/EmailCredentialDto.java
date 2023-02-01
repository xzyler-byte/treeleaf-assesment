package com.xzyler.microservices.blogservice.dto.email;

import com.xzyler.microservices.blogservice.constants.FieldErrorConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailCredentialDto {
    private Long id;

    @NotNull(message = FieldErrorConstants.NOT_NULL)
    @NotBlank(message = FieldErrorConstants.NOT_BLANK)
    @Email
    private String email;

    @NotNull(message = FieldErrorConstants.NOT_NULL)
    @NotBlank(message = FieldErrorConstants.NOT_BLANK)
    private String password;

    @NotNull(message = FieldErrorConstants.NOT_NULL)
    @NotBlank(message = FieldErrorConstants.NOT_BLANK)
    private String host;

    @NotNull(message = FieldErrorConstants.NOT_NULL)
    private String port;

    @NotNull(message = FieldErrorConstants.NOT_NULL)
    private String protocol;

    @NotNull(message = FieldErrorConstants.NOT_NULL)
    List<EmailPropertiesDto> properties;

}