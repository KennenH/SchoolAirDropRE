package com.example.schoolairdroprefactoredition.domain;

public class DomainResult {

    /**
     * success : true
     */
    private int code;

    private long time;

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return code == 200;
    }
}
