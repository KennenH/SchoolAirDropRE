package com.example.schoolairdroprefactoredition.cache;

import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;

public class UserInfoCache {
    public static final String USER_INFO = "infoinfoiminfo";

    private DomainUserInfo.DataBean info;

    public DomainUserInfo.DataBean getInfo() {
        return info;
    }

    public void setInfo(DomainUserInfo.DataBean info) {
        this.info = info;
    }
}
