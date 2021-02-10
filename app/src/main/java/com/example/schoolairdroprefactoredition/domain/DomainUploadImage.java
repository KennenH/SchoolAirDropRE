package com.example.schoolairdroprefactoredition.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DomainUploadImage implements Serializable {

    @Override
    public String toString() {
        return "DomainUploadImage{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", time='" + time + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * code : 200
     * msg :
     * time : 1612939907
     * data : {"images":"/uploads/img/goods/20210210/2f5d551f3092a61e05ffe5ea4aaea9f2.png,","cover":"/uploads/img/goods/20210210/1e9b3e41b1fbdcf96d6b37a6ca6cf213.png"}
     */

    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("time")
    private String time;
    /**
     * images : /uploads/img/goods/20210210/2f5d551f3092a61e05ffe5ea4aaea9f2.png,
     * cover : /uploads/img/goods/20210210/1e9b3e41b1fbdcf96d6b37a6ca6cf213.png
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

    /**
     * 若上传的是物品和帖子的图片将会返回images和cover
     * 否则只返回images，cover为空
     */
    public static class DataBean implements Serializable {

        @Override
        public String toString() {
            return "DataBean{" +
                    "images='" + images + '\'' +
                    ", cover='" + cover + '\'' +
                    '}';
        }

        @SerializedName("images")
        private String images;
        @SerializedName("cover")
        private String cover;

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
    }
}
