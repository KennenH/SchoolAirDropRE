package com.example.schoolairdroprefactoredition.domain;

import java.io.Serializable;

public class DomainGetUserInfo implements Serializable {

    @Override
    public String toString() {
        return "DomainGetUserInfo{" +
                "success=" + success +
                ", user_info=" + user_info +
                '}';
    }

    /**
     * success : true
     * user_info : {
     * "uid":"100002",
     * "uname":"user_100002",
     * "ugender":"m",
     * "uphone":null,
     * "user_img_path":"http://106.54.110.46/Avatars/default.jpg"
     * }
     */

    private boolean success;
    private UserInfoBean user_info;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public static class UserInfoBean {

        @Override
        public String toString() {
            return "UserInfoBean{" +
                    "uid='" + uid + '\'' +
                    ", uname='" + uname + '\'' +
                    ", ugender='" + ugender + '\'' +
                    ", uphone=" + uphone +
                    ", user_img_path='" + user_img_path + '\'' +
                    '}';
        }

        /**
         * uid : 100002
         * uname : user_100002
         * ugender : m
         * uphone : null
         * user_img_path : http://106.54.110.46/Avatars/default.jpg
         */

        private String uid;
        private String uname;
        private String ugender;
        private Object uphone;
        private String user_img_path;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getUgender() {
            return ugender;
        }

        public void setUgender(String ugender) {
            this.ugender = ugender;
        }

        public Object getUphone() {
            return uphone;
        }

        public void setUphone(Object uphone) {
            this.uphone = uphone;
        }

        public String getUser_img_path() {
            return user_img_path;
        }

        public void setUser_img_path(String user_img_path) {
            this.user_img_path = user_img_path;
        }
    }
}
