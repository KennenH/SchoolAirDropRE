package com.example.schoolairdroprefactoredition.domain;

public class DomainUploadToken {

    public static String UPLOAD_TOKEN_KEY = "domain_upload_token";

    private int code;

    private String msg;

    private String upload_token;

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

    public String getUpload_token() {
        return upload_token;
    }

    public void setUpload_token(String upload_token) {
        this.upload_token = upload_token;
    }
}
