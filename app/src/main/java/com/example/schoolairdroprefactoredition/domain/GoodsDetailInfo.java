package com.example.schoolairdroprefactoredition.domain;

import java.io.Serializable;
import java.util.List;

public class GoodsDetailInfo implements Serializable {

    /**
     * success : true
     * message : browse success
     * data : [{"goods_img_set":"assets/goods/img/RvH7nlsdYFjA2coI_1.jpg","goods_description":"不会让别人看到别人","seller_img":"/assets/user/avatars/14_20201209000844.jpg"}]
     */

    private boolean success;
    private String message;

    /**
     * goods_img_set : assets/goods/img/RvH7nlsdYFjA2coI_1.jpg
     * goods_description : 不会让别人看到别人
     * seller_img : /assets/user/avatars/14_20201209000844.jpg
     */

    private List<DataBean> data;
    /**
     * is_in_favor : false
     */

    private boolean is_in_favor;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public boolean isIs_in_favor() {
        return is_in_favor;
    }

    public void setIs_in_favor(boolean is_in_favor) {
        this.is_in_favor = is_in_favor;
    }

    public static class DataBean implements Serializable {

        @Override
        public String toString() {
            return "DataBean{" +
                    "goods_img_set='" + goods_img_set + '\'' +
                    ", goods_description='" + goods_description + '\'' +
                    ", seller_img='" + seller_img + '\'' +
                    ", uid=" + uid +
                    '}';
        }

        private String goods_img_set;
        private String goods_description;
        private String seller_img;
        private int uid;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getGoods_img_set() {
            return goods_img_set;
        }

        public void setGoods_img_set(String goods_img_set) {
            this.goods_img_set = goods_img_set;
        }

        public String getGoods_description() {
            return goods_description;
        }

        public void setGoods_description(String goods_description) {
            this.goods_description = goods_description;
        }

        public String getSeller_img() {
            return seller_img;
        }

        public void setSeller_img(String seller_img) {
            this.seller_img = seller_img;
        }
    }
}
