package com.example.schoolairdroprefactoredition.domain;


import java.io.Serializable;
import java.util.List;

public class DomainGoodsInfo implements Serializable {

    @Override
    public String toString() {
        return "DomainGoodsInfo{" +
                "success=" + success +
                ", data=" + data +
                '}';
    }

    /**
     * success : true
     * data : [{"goods_name":"就好哈哈哈哈","goods_img_cover":"assets/goods/img/1600834532433_0.jpg","goods_img_set":"assets/goods/img/1600834532433_1.jpg&assets/goods/img/1600834532433_2.jpg","goods_id":1024,"goods_price":466,"goods_longitude":120.360638,"goods_latitude":30.317723,"goods_is_quotable":1,"goods_is_brandNew":0,"goods_description":"就好像将心比心吧你下班","seller_info":{"uid":15,"uname":"iken","credit_num":2,"user_img_path":"/assets/user/avatars/15_20200923145227.jpg"}}]
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
                    "goods_name='" + goods_name + '\'' +
                    ", goods_img_cover='" + goods_img_cover + '\'' +
                    ", goods_img_set='" + goods_img_set + '\'' +
                    ", goods_id=" + goods_id +
                    ", goods_price=" + goods_price +
                    ", goods_longitude=" + goods_longitude +
                    ", goods_latitude=" + goods_latitude +
                    ", goods_is_quotable=" + goods_is_quotable +
                    ", goods_is_brandNew=" + goods_is_brandNew +
                    ", goods_description='" + goods_description + '\'' +
                    ", seller_info=" + seller_info +
                    '}';
        }

        /**
         * goods_name : 就好哈哈哈哈
         * goods_img_cover : assets/goods/img/1600834532433_0.jpg
         * goods_img_set : assets/goods/img/1600834532433_1.jpg&assets/goods/img/1600834532433_2.jpg
         * goods_id : 1024
         * goods_price : 466
         * goods_longitude : 120.360638
         * goods_latitude : 30.317723
         * goods_is_quotable : 1
         * goods_is_brandNew : 0
         * goods_description : 就好像将心比心吧你下班
         * seller_info : {"uid":15,"uname":"iken","credit_num":2,"user_img_path":"/assets/user/avatars/15_20200923145227.jpg"}
         */

        private String goods_name;
        private String goods_img_cover;
        private String goods_img_set;
        private String goods_id;
        private String goods_price;
        private String goods_longitude;
        private String goods_latitude;
        private int goods_is_quotable;
        private int goods_is_brandNew;
        private String goods_description;
        private SellerInfoBean seller_info;

        private int quote_id;
        private String sender_id;
        private String quote_price;
        private int state;

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_img_cover() {
            return goods_img_cover;
        }

        public void setGoods_img_cover(String goods_img_cover) {
            this.goods_img_cover = goods_img_cover;
        }

        public String getGoods_img_set() {
            return goods_img_set;
        }

        public void setGoods_img_set(String goods_img_set) {
            this.goods_img_set = goods_img_set;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getGoods_longitude() {
            return goods_longitude;
        }

        public void setGoods_longitude(String goods_longitude) {
            this.goods_longitude = goods_longitude;
        }

        public String getGoods_latitude() {
            return goods_latitude;
        }

        public void setGoods_latitude(String goods_latitude) {
            this.goods_latitude = goods_latitude;
        }

        public int getGoods_is_quotable() {
            return goods_is_quotable;
        }

        public void setGoods_is_quotable(int goods_is_quotable) {
            this.goods_is_quotable = goods_is_quotable;
        }

        public int getGoods_is_brandNew() {
            return goods_is_brandNew;
        }

        public void setGoods_is_brandNew(int goods_is_brandNew) {
            this.goods_is_brandNew = goods_is_brandNew;
        }

        public String getGoods_description() {
            return goods_description;
        }

        public void setGoods_description(String goods_description) {
            this.goods_description = goods_description;
        }

        public int getQuote_id() {
            return quote_id;
        }

        public void setQuote_id(int quote_id) {
            this.quote_id = quote_id;
        }

        public String getSender_id() {
            return sender_id;
        }

        public void setSender_id(String sender_id) {
            this.sender_id = sender_id;
        }

        public String getQuote_price() {
            return quote_price;
        }

        public void setQuote_price(String quote_price) {
            this.quote_price = quote_price;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public SellerInfoBean getSeller_info() {
            return seller_info;
        }

        public void setSeller_info(SellerInfoBean seller_info) {
            this.seller_info = seller_info;
        }

        public static class SellerInfoBean implements Serializable {
            @Override
            public String toString() {
                return "SellerInfoBean{" +
                        "uid=" + uid +
                        ", uname='" + uname + '\'' +
                        ", credit_num=" + credit_num +
                        ", user_img_path='" + user_img_path + '\'' +
                        '}';
            }

            /**
             * uid : 15
             * uname : iken
             * credit_num : 2
             * user_img_path : /assets/user/avatars/15_20200923145227.jpg
             */


            private int uid;
            private String uname;
            private int credit_num;
            private String user_img_path;

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

            public int getCredit_num() {
                return credit_num;
            }

            public void setCredit_num(int credit_num) {
                this.credit_num = credit_num;
            }

            public String getUser_img_path() {
                return user_img_path;
            }

            public void setUser_img_path(String user_img_path) {
                this.user_img_path = user_img_path;
            }
        }
    }
}
