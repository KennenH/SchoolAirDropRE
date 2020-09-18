package com.example.schoolairdroprefactoredition.cache;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;

public class UserTokenCache {
    public static final String USER_TOKEN = "imatoken";

    private DomainAuthorize token;

    public DomainAuthorize getToken() {
        return token;
    }

    public void setToken(DomainAuthorize token) {
        this.token = token;
    }
}
