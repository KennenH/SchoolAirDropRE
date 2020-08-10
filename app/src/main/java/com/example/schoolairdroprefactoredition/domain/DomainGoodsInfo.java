package com.example.schoolairdroprefactoredition.domain;


import java.io.Serializable;
import java.util.List;

public class DomainGoodsInfo {

    @Override
    public String toString() {
        return "DomainGoodsInfo{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", goodsInfo=" + goodsInfo +
                '}';
    }

    /**
     * success : true
     * message : position send success
     * goodsInfo : [{"goodid":"10004","cover":"http://text4.jpg","picset":"http://test4.jpg&http://ttext.jpg","tital":"hkn","price":"10000","IsPrice":"1","Istender":"1","Description":"This is a test"}]
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
         * goodid : 10004
         * cover : http://text4.jpg
         * picset : http://test4.jpg&http://ttext.jpg
         * tital : hkn
         * price : 10000
         * IsPrice : 1
         * Istender : 1
         * Description : This is a test
         */

        private String goodid;
        private String cover;
        private String picset;
        private String tital;
        private String price;
        private String IsPrice;
        private String Istender;
        private String Description;

        public String getGoodid() {
            return goodid;
        }

        public void setGoodid(String goodid) {
            this.goodid = goodid;
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

        public String getTital() {
            return tital;
        }

        public void setTital(String tital) {
            this.tital = tital;
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
    }
}
