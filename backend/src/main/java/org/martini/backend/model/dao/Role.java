package org.martini.backend.model.dao;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_ADMIN(Constants.ADMIN),
    ROLE_USER(Constants.USER);

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public static class Constants {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USER = "ROLE_USER";
    }
}
