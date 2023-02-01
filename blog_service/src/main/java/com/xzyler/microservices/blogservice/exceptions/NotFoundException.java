package com.xzyler.microservices.blogservice.exceptions;

/**
 * This  class handel's NotFoundException
 *
 * @author Nitesh Thapa
 * @version 1.0
 * @since 1.0 - 2021
 * @for Security service
 */
public class NotFoundException extends Exception {
    public NotFoundException(String e) {
        super(e);
    }
}
