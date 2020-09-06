package com.example.schoolairdroprefactoredition.domain;


import java.io.Serializable;
import java.util.List;

public class DomainGoodsInfo {
    @Override
    public String toString() {
        return "DomainGoodsInfo{" +
                "success=" + success +
                ", data=" + data +
                '}';
    }

    /**
     * success : true
     * data : [
     * {"goods_name":"哈士奇",
     * "goods_img_cover":"",
     * "goods_img_set":"",
     * "goods_id":"3",
     * "goods_price":2,
     * "goods_is_quotable":0,
     * "goods_is_brandNew":1,
     * "goods_description":"114",
     * "seller_info":{"uid":15,"uname":"user_15","credit_num":2,"user_img_path":"http://106.54.110.46/public/assets/user/avatars/default.jpg"}},
     *
     * {"goods_name":"金毛",
     * "goods_img_cover":"",
     * "goods_img_set":"",
     * "goods_id":"2",
     * "goods_price":11,
     * "goods_is_quotable":0,
     * "goods_is_brandNew":1,
     * "goods_description":"111",
     * "seller_info":{"uid":15,"uname":"user_15","credit_num":2,"user_img_path":"http://106.54.110.46/public/assets/user/avatars/default.jpg"}}]
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
                    ", goods_id='" + goods_id + '\'' +
                    ", goods_price=" + goods_price +
                    ", goods_is_quotable=" + goods_is_quotable +
                    ", goods_is_brandNew=" + goods_is_brandNew +
                    ", goods_description='" + goods_description + '\'' +
                    ", seller_info=" + seller_info +
                    '}';
        }

        /**
         * goods_name : 哈士奇
         * goods_img_cover :
         * goods_img_set :
         * goods_id : 3
         * goods_price : 2
         * goods_is_quotable : 0
         * goods_is_brandNew : 1
         * goods_description : 114
         * seller_info : {"uid":15,"uname":"user_15","credit_num":2,"user_img_path":"http://106.54.110.46/public/assets/user/avatars/default.jpg"}
         */

        private String goods_name;
        private String goods_img_cover;
        private String goods_img_set;
        private String goods_id;
        private int goods_price;
        private int goods_is_quotable;
        private int goods_is_brandNew;
        private String goods_description;
        private SellerInfoBean seller_info;

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

        public int getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(int goods_price) {
            this.goods_price = goods_price;
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

        public SellerInfoBean getSeller_info() {
            return seller_info;
        }

        public void setSeller_info(SellerInfoBean seller_info) {
            this.seller_info = seller_info;
        }

        public static class SellerInfoBean implements Serializable {
            /**
             * uid : 15
             * uname : user_15
             * credit_num : 2
             * user_img_path : http://106.54.110.46/public/assets/user/avatars/default.jpg
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
