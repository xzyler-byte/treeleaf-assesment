package com.xzyler.microservices.securityservice.enums;

public enum Gender {

    MALE(1, "Male"),
    FEMALE(2, "Female"),
    OTHERS(3, "Others");

    private Integer key;
    private String value;

    Gender(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public static Gender getByKey(Integer key) {
        if (key == null)
            throw new IllegalArgumentException("Gender cannot be null");
        Gender[] genders = values();
        for (Gender gender : genders) {
            if (key.equals(gender.key)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("UnIdentified Gender");
    }
}
