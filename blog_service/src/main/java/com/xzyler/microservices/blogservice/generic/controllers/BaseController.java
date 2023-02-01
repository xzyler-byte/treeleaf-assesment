package com.xzyler.microservices.blogservice.generic.controllers;


import com.xzyler.microservices.blogservice.dto.generics.GlobalApiResponse;
import com.xzyler.microservices.blogservice.util.CustomMessageSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base Controller
 */
public class BaseController {

    /**
     * Global API Response Instance
     */
    @Autowired
    protected GlobalApiResponse globalApiResponse;

    /**
     * API Success Status
     */
    protected final boolean API_SUCCESS_STATUS = true;

    /**
     * API Error Status
     */
    protected final boolean API_ERROR_STATUS = false;

    /**
     * Message Source Instance
     */
    @Autowired
    protected CustomMessageSource customMessageSource;

    /**
     * Module Name
     */
    protected String moduleName;
    protected String permissionName;

    /**
     * Function that sends successful API Response
     *
     * @param message
     * @param data
     * @return
     */
    protected GlobalApiResponse successResponse(String message, Object data) {
        globalApiResponse = new GlobalApiResponse();
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
    protected GlobalApiResponse errorResponse(String message, Object errors) {
        globalApiResponse=new GlobalApiResponse();
        globalApiResponse.setStatus(API_ERROR_STATUS);
        globalApiResponse.setMessage(message);
        globalApiResponse.setErrors(errors);
        return globalApiResponse;
    }

    public String getPermissionName() {
        return permissionName;
    }

}
