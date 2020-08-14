package com.example.schoolairdroprefactoredition.domain;


import java.io.Serializable;
import java.util.List;

public class DomainGoodsInfo {

    /**
     * success : true
     * message : position send success
     * goodsInfo : [{"goodsid":"10004","cover":"http://text4.jpg","picset":"http://test4.jpg&http://ttext.jpg","title":"hkn","price":"10000","IsPrice":"1","Istender":"1","Description":"This is a test","sellerid":"100001","uname":"user100001","user_img_path":null,"credit_num":"100"}]
     */

    private boolean success;
    private String message;
    private List<GoodsInfoBean> goodsInfo;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<GoodsInfoBean> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<GoodsInfoBean> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public static class GoodsInfoBean implements Serializable {
        /**
         * goodsid : 10004
         * cover : http://text4.jpg
         * picset : http://test4.jpg&http://ttext.jpg
         * title : hkn
         * price : 10000
         * IsPrice : 1
         * Istender : 1
         * Description : This is a test
         * sellerid : 100001
         * uname : user100001
         * user_img_path : null
         * credit_num : 100
         */

        private String goodsid;
        private String cover;
        private String picset;
        private String title;
        private String price;
        private String IsPrice;
        private String Istender;
        private String Description;
        private String sellerid;
        private String uname;
        private Object user_img_path;
        private String credit_num;

        public String getGoodsid() {
            return goodsid;
        }

        public void setGoodsid(String goodsid) {
            this.goodsid = goodsid;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getPicset() {
            return picset;
        }

        public void setPicset(String picset) {
            this.picset = picset;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getIsPrice() {
            return IsPrice;
        }

        public void setIsPrice(String IsPrice) {
            this.IsPrice = IsPrice;
        }

        public String getIstender() {
            return Istender;
        }

        public void setIstender(String Istender) {
            this.Istender = Istender;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public String getSellerid() {
            return sellerid;
        }

        public void setSellerid(String sellerid) {
            this.sellerid = sellerid;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public Object getUser_img_path() {
            return user_img_path;
        }

        public void setUser_img_path(Object user_img_path) {
            this.user_img_path = user_img_path;
        }

        public String getCredit_num() {
            return credit_num;
        }

        public void setCredit_num(String credit_num) {
            this.credit_num = credit_num;
        }
    }
}
