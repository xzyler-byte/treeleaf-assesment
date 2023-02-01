package com.xzyler.microservices.blogservice.util;

import java.util.UUID;

public class RandomUtils {

    public static String generateRandomUuid() {
        return UUID.randomUUID().toString();
    }
}
