package com.example.schoolairdroprefactoredition.domain;

import com.google.gson.annotations.SerializedName;

public class DomainUpload {

    /**
     * code : 200
     * msg : upload success
     * time : 1613535729
     * data : {"url":"/uploads/img/im/20210217/4aae21a131e88c2c6fe22741d54878ff.png"}
     */

    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("time")
    private String time;
    /**
     * url : /uploads/img/im/20210217/4aae21a131e88c2c6fe22741d54878ff.png
     */

    @SerializedName("data")
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        @SerializedName("url")
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
