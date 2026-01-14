package com.podflix.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private final int tokenVersion;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
            int tokenVersion) {
        super(username, password, authorities);
        this.tokenVersion = tokenVersion;
    }

    public int getTokenVersion() {
        return tokenVersion;
    }
}
