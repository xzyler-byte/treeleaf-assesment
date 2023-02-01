package com.xzyler.microservices.blogservice.exceptions;

import com.xzyler.microservices.blogservice.response.APIResponse;
import com.xzyler.microservices.blogservice.util.CustomMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * This  class handel's Oauth2ExceptionTranslatorConfiguration
 *
 * @author Nitesh Thapa
 * @version 1.0
 * @since 1.0 - 2021
 * @for Security service
 */
@Configuration
public class Oauth2ExceptionTranslatorConfiguration {

    @Autowired
    CustomMessageSource messages;

    @Bean
    public WebResponseExceptionTranslator oauth2ResponseExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {

            @Override
            public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {

                ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
                OAuth2Exception body = responseEntity.getBody();
                HttpStatus statusCode = responseEntity.getStatusCode();

                body.addAdditionalInformation("status", Integer.valueOf(body.getHttpErrorCode()).toString());
                body.addAdditionalInformation("message", e.getMessage());
                //body.addAdditionalInformation("code", body.getOAuth2ErrorCode().toUpperCase());

                HttpHeaders headers = new HttpHeaders();
                headers.setAll(responseEntity.getHeaders().toSingleValueMap());
                // do something with header or response
                //TODO: Return Message Based on statusCode
                String message = e.getMessage();
                if(statusCode == HttpStatus.UNAUTHORIZED) {
                    message = messages.get("api.auth.failed");
                }
                return new ResponseEntity(prepareAuthErrorResponse(message, body), headers, statusCode);
            }
        };
    }
    /**
     * Prepare Error Response
     * @param message
     * @param errors
     * @return
     */
    private APIResponse prepareAuthErrorResponse(String message, Object errors) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatus(false);
        apiResponse.setMessage(message);
        apiResponse.setErrors(errors);
        return apiResponse;
    }
}
