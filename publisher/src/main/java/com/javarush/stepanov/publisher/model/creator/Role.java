package com.javarush.stepanov.publisher.model.creator;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,CUSTOMER,GUEST,MODERATOR,USER;

    @Override
    public String getAuthority() {
        return this.name().toUpperCase();
    }
}
