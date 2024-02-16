package org.example.security.entity;

public enum AuthorityEnum {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String value;

    AuthorityEnum(String value) {
        this.value = value;
    }

    public String getValue() { return value; }
}
