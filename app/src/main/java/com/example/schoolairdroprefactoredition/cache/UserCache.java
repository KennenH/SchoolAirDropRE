package com.example.schoolairdroprefactoredition.cache;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;

public class UserCache {
    public static final String USER_CACHE = "imatoken";

    private DomainAuthorize token;
    private DomainUserInfo.DataBean info;

    public DomainUserInfo.DataBean getInfo() {
        return info;
    }

    public void setInfo(DomainUserInfo.DataBean info) {
        this.info = info;
    }

    public DomainAuthorize getToken() {
        return token;
    }

    public void setToken(DomainAuthorize token) {
        this.token = token;
    }
}
