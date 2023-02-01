package com.xzyler.microservices.securityservice.dto.generics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@Scope("prototype")
public class GlobalApiResponse implements Serializable {
    private boolean status;
    private String message;

    private Object data;
    private Object errors;

    public void setResponse(String message, boolean status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public GlobalApiResponse setResponse(String message, Object data) {
        this.message = message;
        this.data = data;
        this.status = true;
        this.errors = false;
        return this;
    }

    public boolean isStatus() {
        return status;
    }


    public ResponseEntity<GlobalApiResponse> ok(String message, Object data) {
        this.message = message;
        this.data = data;
        this.status = true;
        this.errors = false;
        return ResponseEntity.ok(this);
    }
}
