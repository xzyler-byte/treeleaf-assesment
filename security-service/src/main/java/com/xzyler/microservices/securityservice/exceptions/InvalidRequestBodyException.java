package com.xzyler.microservices.securityservice.exceptions;

/**
 * This  class handel's InvalidRequestBodyException
 *
 * @author Nitesh Thapa
 * @version 1.0
 * @since 1.0 - 2021
 * @for Security service
 */
public class InvalidRequestBodyException extends RuntimeException {
    public InvalidRequestBodyException(String e) {
        super(e);
    }
}
