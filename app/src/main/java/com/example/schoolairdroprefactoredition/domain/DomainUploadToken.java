package com.example.schoolairdroprefactoredition.domain;

import java.io.Serializable;

public class DomainUploadToken {

    public static String UPLOAD_TOKEN_KEY = "domain_upload_token";

    private int code;

    private String msg;

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        @Override
        public String toString() {
            return "DataBean{" +
                    "token='" + token + '\'' +
                    ", expire=" + expire +
                    '}';
        }

        private String token;

        private long expire;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getExpire() {
            return expire;
        }

        public void setExpire(long expire) {
            this.expire = expire;
        }
    }

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
}
