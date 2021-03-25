package com.example.schoolairdroprefactoredition.domain;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author kennen
 * @date 2021/3/25
 */
public class DomainToken implements Serializable {

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getAccess_token() {
        return data.access_token;
    }

    @NotNull
    @Override
    public String toString() {
        return "DomainToken{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean implements Serializable {
        @NotNull
        @Override
        public String toString() {
            return "DataBean{" +
                    "access_token='" + access_token + '\'' +
                    ", expires_in=" + expires_in +
                    ", token_type='" + token_type + '\'' +
                    ", scope='" + scope + '\'' +
                    '}';
        }

        private String access_token;

        private Long expires_in;

        private String token_type;

        private String scope;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public Long getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(Long expires_in) {
            this.expires_in = expires_in;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }
}

