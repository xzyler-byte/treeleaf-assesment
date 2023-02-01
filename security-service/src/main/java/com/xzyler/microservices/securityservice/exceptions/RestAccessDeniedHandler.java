package com.xzyler.microservices.securityservice.exceptions;

import com.xzyler.microservices.securityservice.response.APIResponse;
import com.xzyler.microservices.securityservice.util.CustomMessageSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Handles Forbidden/ Access Denied API Response
 * HttpStatus Code: 403
 * @author Nitesh Thapa
 * @version 1.0
 * @since 1.0 - 2021
 * @for Security service
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    CustomMessageSource messages;

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.addHeader("Content-Type", "application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);

        APIResponse response = new APIResponse();
        response.setStatus(false);
        response.setMessage(messages.get("api.request.access_denied"));
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, response);
        out.flush();
    }
}
