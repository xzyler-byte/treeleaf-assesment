package com.xzyler.microservices.securityservice.response;

import com.xzyler.microservices.securityservice.util.CustomMessageSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Scope("prototype")
public class APIResponse {

    @Autowired
    private CustomMessageSource messageSource;

    @NotNull
    private boolean status;
    @NotNull
    private String message;

    private Object data;

    private Object errors;

    public APIResponse(boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public APIResponse successResponse(String message, Object data) {
        /*this.message = message;
        this.data= data;
        this.status=true;*/
        return new APIResponse(true, message, data);
    }

    public ResponseEntity<?> saveSuccess(String message, Object data) {
        this.message = messageSource.get("success.save", messageSource.get(message));
        return new ResponseEntity(successResponse(this.message, data), HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateSuccess(String message, Object data) {
        this.message = messageSource.get("success.update", messageSource.get(message));
        return new ResponseEntity(successResponse(this.message, data), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<?> deleteSuccess(String message, Object data) {
        this.message = messageSource.get("success.delete", messageSource.get(message));
        return new ResponseEntity(successResponse(this.message, data), HttpStatus.OK);
    }

    public ResponseEntity<?> getSuccess(String message, Object data) {
        this.message = messageSource.get("success.get", messageSource.get(message));
        return new ResponseEntity(successResponse(this.message, data), HttpStatus.OK);
    }

}
