package com.xzyler.microservices.blogservice.controller;

import com.xzyler.microservices.blogservice.exceptions.InvalidRequestBodyException;
import com.xzyler.microservices.blogservice.response.APIResponse;
import com.xzyler.microservices.blogservice.util.CustomMessageSource;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * This controller class contains all the generic component required for all extended classes
 *
 * @version 1.0-2021
 * @for Security service
 * @since 2021
 */
@SecurityRequirement(name = "oauth2")
public class BaseController {

    /**
     * API Success Status
     */
    protected final boolean API_SUCCESS_STATUS = true;
    /**
     * API Error Status
     */
    protected final boolean API_ERROR_STATUS = false;
    /**
     * Global API Response Instance
     */

    protected APIResponse globalApiResponse = new APIResponse();
    /**
     * Message Source Instance
     */
    @Autowired
    protected CustomMessageSource customMessageSource;

    /**
     * Module Name
     */

    protected String moduleName;
    protected String permissionName = this.getClass().getSimpleName();

    /**
     * Function that sends successful API Response
     *
     * @param message
     * @param data
     * @return
     */
    protected APIResponse successResponse(String message, Object data) {
        globalApiResponse.setStatus(API_SUCCESS_STATUS);
        globalApiResponse.setMessage(message);
        globalApiResponse.setData(data);
        return globalApiResponse;
    }

    /**
     * Function that sends error API Response
     *
     * @param message
     * @param errors
     * @return
     */
    protected APIResponse errorResponse(String message, Object errors) {
        globalApiResponse.setStatus(API_ERROR_STATUS);
        globalApiResponse.setMessage(message);
        globalApiResponse.setData(errors);
        return globalApiResponse;
    }

    /**
     * Function that validates request body
     *
     * @param bindingResults
     * @throws InvalidRequestBodyException
     */
    protected void validateRequestBody(BindingResult bindingResults) throws InvalidRequestBodyException {
        if (bindingResults.hasErrors()) {
            List<FieldError> errors = bindingResults.getFieldErrors();
            StringBuilder error_definition = new StringBuilder();
            for (FieldError error : errors) {
                error_definition.append(error.getDefaultMessage()).append(",");
            }
            throw new InvalidRequestBodyException(error_definition.toString());
        }
    }

    /**
     * Function to fetch Permission name
     *
     * @return
     */
    public String getPermissionName() {
        return permissionName;
    }

}
