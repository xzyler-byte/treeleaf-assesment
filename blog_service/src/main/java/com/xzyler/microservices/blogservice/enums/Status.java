package com.xzyler.microservices.blogservice.enums;

public enum Status {
    NEW(0, "New"),
    ACTIVE(1, "Active"),
    INACTIVE(2, "Inactive"),
    DELETED(3,"Deleted");

    private Integer key;
    private String value;


    Status(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static Status getByKey(Integer key) {
        if (key == null)
            throw new IllegalArgumentException("Key cannot be null");
        Status[] Statuss = values();
        for (Status status : Statuss) {
            if (key.equals(status.key)) {
                return status;
            }
        }
        throw new IllegalArgumentException("UnIdentified Status");
    }
}
