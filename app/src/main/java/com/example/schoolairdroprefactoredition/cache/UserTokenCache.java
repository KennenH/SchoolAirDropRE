package com.example.schoolairdroprefactoredition.cache;

import com.example.schoolairdroprefactoredition.domain.DomainToken;

public class UserTokenCache {
    public static final String USER_TOKEN = "imatoken";

    private DomainToken token;

    public DomainToken getToken() {
        return token;
    }

    public void setToken(DomainToken token) {
        this.token = token;
    }
}
