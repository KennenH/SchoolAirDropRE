package com.example.schoolairdroprefactoredition.domain;

import java.io.Serializable;

public class DomainAuthorize implements Serializable {

    @Override
    public String toString() {
        return "DomainAuthorizePost{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", message='" + message + '\'' +
                ", scope='" + scope + '\'' +
                ", token_type='" + token_type + '\'' +
                '}';
    }

    /**
     * "access_token": "1e488dad5b02256152d0d450cb3d06ddf9839a9a",
     * "expires_in": 3600,
     * "token_type": "Bearer",
     * "scope": "basic",
     * "message": "success"
     */
    private String access_token;
    private int expires_in;
    private String message;
    private String scope;
    private String token_type;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
}
