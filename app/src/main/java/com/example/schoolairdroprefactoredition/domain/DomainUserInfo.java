package com.example.schoolairdroprefactoredition.domain;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * 禁止在fragment和activity中直接传递本类对象
 * 应统一传递{@link DataBean}，只有在viewmodel
 * 中和repository中才直接使用本类对象
 */
public class DomainUserInfo implements Serializable {

    @NotNull
    @Override
    public String toString() {
        return "DomainUserInfo{" +
                ", msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }

    @SerializedName("msg")
    private String msg;
    @SerializedName("code")
    private int code;
    @SerializedName("data")
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        @NotNull
        @Override
        public String toString() {
            return "DataBean{" +
                    "userId=" + userId +
                    ", userName='" + userName + '\'' +
                    ", userAvatar='" + userAvatar + '\'' +
                    ", userGender='" + userGender + '\'' +
                    ", createtime=" + createtime +
                    ", lastLoginTime='" + lastLoginTime + '\'' +
                    ", userGoodsOnSaleCount=" + userGoodsOnSaleCount +
                    ", userContactCount=" + userContactCount +
                    '}';
        }

        /**
         * "user_id": 7,
         * "user_name": "user_7",
         * "user_avatar": "/uploads/img/user/default/default.jpg",
         * "user_gender": "m",
         * "createtime": 1612696413,
         * "last_login_time": "2021-03-05 17:03:30"
         * "user_goodsOnSaleCount": 0,
         * "user_contactCount": 0
         */

        @SerializedName("user_id")
        private int userId;
        @SerializedName("user_name")
        private String userName;
        @SerializedName("user_avatar")
        private String userAvatar;
        @SerializedName("user_gender")
        private String userGender;
        @SerializedName("createtime")
        private long createtime;
        @SerializedName("last_login_time")
        private String lastLoginTime;
        @SerializedName("user_goodsOnSaleCount")
        private int userGoodsOnSaleCount;
        @SerializedName("user_contactCount")
        private int userContactCount;

        public String getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(String lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public int getUserGoodsOnSaleCount() {
            return userGoodsOnSaleCount;
        }

        public void setUserGoodsOnSaleCount(int selling) {
            this.userGoodsOnSaleCount = selling;
        }

        public int getUserContactCount() {
            return userContactCount;
        }

        public void setUserContactCount(int bought) {
            this.userContactCount = bought;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserGender() {
            return userGender;
        }

        public void setUserGender(String userGender) {
            this.userGender = userGender;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }
    }
}
