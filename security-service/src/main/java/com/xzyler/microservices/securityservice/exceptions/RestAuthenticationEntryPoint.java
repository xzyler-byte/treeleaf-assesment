package com.xzyler.microservices.securityservice.exceptions;

import com.xzyler.microservices.securityservice.response.APIResponse;
import com.xzyler.microservices.securityservice.util.CustomMessageSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    CustomMessageSource messages;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //Set Header and Http Code
        httpServletResponse.addHeader("Content-Type", "application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        APIResponse response = new APIResponse();
        response.setStatus(false);
        response.setMessage(messages.get("api.request.unauthorized"));
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, response);
        out.flush();
    }
}
