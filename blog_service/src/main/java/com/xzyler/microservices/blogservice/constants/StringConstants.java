package com.xzyler.microservices.blogservice.constants;

import org.springframework.data.domain.Sort;

/**
 * String Constants
 */
public class StringConstants {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String PRESENT_DIR = System.getProperty("user.dir");

    public static final String ALL = "All";
    public static final String ACTIVE = "Active";
    public static final String INACTIVE = "Inactive";
    public static final String DATE_PATTERN = "yyyy/MM/dd";
    public static final String DATE_PATTERN1 = "yyyy-MM-dd";
    public static final String LOCAL_PATTERN = "[\\u0900-\\u097F ]+";
    public static final String UPLOAD_DIR = "/home/test-docs/";
    public static final Object THREADPREFIX = "thread-";

    public static final Sort.Direction down = Sort.Direction.DESC;
    public static final Sort.Direction up = Sort.Direction.ASC;
}
