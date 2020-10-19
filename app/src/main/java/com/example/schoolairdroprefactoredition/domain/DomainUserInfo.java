package com.example.schoolairdroprefactoredition.domain;

import java.io.Serializable;
import java.util.List;

public class DomainUserInfo implements Serializable {

    @Override
    public String toString() {
        return "DomainGetUserInfo{" +
                "success=" + success +
                ", data=" + data +
                '}';
    }

    /**
     * success : true
     * data : [{"uid":15,"uname":"user_15","ugender":"m","user_img_path":"http://106.54.110.46/public/assets/user/avatars/default.jpg","ualipay":"19858120611","uphone":null,"credit_num":2,"device_token":null}]
     */

    private boolean success;
    private List<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        @Override
        public String toString() {
            return "DataBean{" +
                    "uid=" + uid +
                    ", uname='" + uname + '\'' +
                    ", ugender='" + ugender + '\'' +
                    ", user_img_path='" + user_img_path + '\'' +
                    ", ualipay='" + ualipay + '\'' +
                    ", uphone=" + uphone +
                    ", credit_num=" + credit_num +
                    ", device_token=" + device_token +
                    '}';
        }

        /**
         * uid : 15
         * uname : user_15
         * user_img_path : http://106.54.110.46/public/assets/user/avatars/default.jpg
         * credit_num : 2
         * device_token : null
         */

        private int uid;
        private String uname;
        private String ugender;
        private String user_img_path;
        private String ualipay;
        private String uphone;
        private int credit_num;
        private String device_token;
        private int count_on_sale;
        private int count_sold;
        private int count_bought;

        public int getSelling() {
            return count_on_sale;
        }

        public void setSelling(int selling) {
            this.count_on_sale = selling;
        }

        public int getSold() {
            return count_sold;
        }

        public void setSold(int sold) {
            this.count_sold = sold;
        }

        public int getBought() {
            return count_bought;
        }

        public void setBought(int bought) {
            this.count_bought = bought;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
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

        public String getUser_img_path() {
            return user_img_path;
        }

        public void setUser_img_path(String user_img_path) {
            this.user_img_path = user_img_path;
        }

        public String getUalipay() {
            return ualipay;
        }

        public void setUalipay(String ualipay) {
            this.ualipay = ualipay;
        }

        public Object getUphone() {
            return uphone;
        }

        public void setUphone(String uphone) {
            this.uphone = uphone;
        }

        public int getCredit_num() {
            return credit_num;
        }

        public void setCredit_num(int credit_num) {
            this.credit_num = credit_num;
        }

        public Object getDevice_token() {
            return device_token;
        }

        public void setDevice_token(String device_token) {
            this.device_token = device_token;
        }
    }
}
