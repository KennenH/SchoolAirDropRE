package com.example.schoolairdroprefactoredition.domain;

import java.io.Serializable;
import java.util.List;

public class HomeGoodsListInfo implements Serializable {

    /**
     * success : true
     * data : [{"goods_name":"哈哈哈哈","goods_img_cover":"assets/goods/img/yfmcTw4uFzIkgtXQ_0.jpg","goods_id":1116,"goods_price":99,"watch_count":4,"goods_is_quotable":1,"goods_is_brandNew":0,"seller_info":"weirdo"},{"goods_name":"niko","goods_img_cover":"assets/goods/img/UmMHekJlINW5BYhL_0.jpg","goods_id":1110,"goods_price":1485,"watch_count":10,"goods_is_quotable":1,"goods_is_brandNew":1,"seller_info":"冲冲Developer"},{"goods_name":"在线客服人员的注意","goods_img_cover":"assets/goods/img/HCSVxQvKcpbmkogf_0.jpg","goods_id":1108,"goods_price":120,"watch_count":16,"goods_is_quotable":0,"goods_is_brandNew":0,"seller_info":"冲冲Developer"},{"goods_name":"azazaz","goods_img_cover":"assets/goods/img/6mj9hqYUQpvGSA1M_0.jpg","goods_id":1124,"goods_price":9999,"watch_count":10,"goods_is_quotable":0,"goods_is_brandNew":1,"seller_info":"卢志成sb"},{"goods_name":"低价贱卖二手劳动力","goods_img_cover":"assets/goods/img/759OL4FeobGDtuga_0.jpg","goods_id":1123,"goods_price":10000,"watch_count":3,"goods_is_quotable":0,"goods_is_brandNew":1,"seller_info":"卢志成sb"},{"goods_name":"苏睿是gayking","goods_img_cover":"assets/goods/img/5azm3fFLMr89NqEb_0.jpg","goods_id":1125,"goods_price":100,"watch_count":15,"goods_is_quotable":1,"goods_is_brandNew":0,"seller_info":"clz"},{"goods_name":"哈哈","goods_img_cover":"assets/goods/img/BudvWD7yAlC5SbFz_0.jpg","goods_id":1111,"goods_price":55,"watch_count":3,"goods_is_quotable":1,"goods_is_brandNew":0,"seller_info":"weirdo"},{"goods_name":"嘿嘿","goods_img_cover":"assets/goods/img/ab8Ang6jYLklezJT_0.jpg","goods_id":1113,"goods_price":66,"watch_count":2,"goods_is_quotable":1,"goods_is_brandNew":0,"seller_info":"weirdo"},{"goods_name":"好红红火火恍恍","goods_img_cover":"assets/goods/img/xLzPTa5hYVqwyHkm_0.jpg","goods_id":1126,"goods_price":666,"watch_count":20,"goods_is_quotable":1,"goods_is_brandNew":0,"seller_info":"ikennenx"},{"goods_name":"哈哈哈哈哈哈","goods_img_cover":"assets/goods/img/cghUsZ6qbix42yIv_0.jpg","goods_id":1115,"goods_price":369,"watch_count":8,"goods_is_quotable":1,"goods_is_brandNew":0,"seller_info":"weirdo"},{"goods_name":"这种","goods_img_cover":"assets/goods/img/RvH7nlsdYFjA2coI_0.jpg","goods_id":1094,"goods_price":999,"watch_count":4,"goods_is_quotable":0,"goods_is_brandNew":0,"seller_info":"冲冲Developer"},{"goods_name":"好了","goods_img_cover":"assets/goods/img/qUOzAlDiBnyNI0hG_0.jpg","goods_id":1104,"goods_price":0.05,"watch_count":14,"goods_is_quotable":1,"goods_is_brandNew":1,"seller_info":"冲冲Developer"},{"goods_name":"uuuuuu","goods_img_cover":"assets/goods/img/3ZwWQcuVEyXITnKH_0.jpg","goods_id":1127,"goods_price":99,"watch_count":15,"goods_is_quotable":1,"goods_is_brandNew":0,"seller_info":"ikennenx"},{"goods_name":"我卢本伟没有开挂。","goods_img_cover":"assets/goods/img/fBhaQRund9yc5DO7_0.jpg","goods_id":1114,"goods_price":666.6,"watch_count":7,"goods_is_quotable":1,"goods_is_brandNew":1,"seller_info":"冲冲Developer"},{"goods_name":"roll了roll了","goods_img_cover":"assets/goods/img/VeF0Bxs95h8Lqgry_0.jpg","goods_id":1128,"goods_price":0.001,"watch_count":8,"goods_is_quotable":0,"goods_is_brandNew":0,"seller_info":"冲冲Developer"},{"goods_name":"郑正熊笨蛋","goods_img_cover":"assets/goods/img/MtlLYEOHckPe7hbC_0.jpg","goods_id":1120,"goods_price":0.1,"watch_count":11,"goods_is_quotable":0,"goods_is_brandNew":0,"seller_info":"大帅哥"}]
     */

    private boolean success;
    /**
     * goods_name : 哈哈哈哈
     * goods_img_cover : assets/goods/img/yfmcTw4uFzIkgtXQ_0.jpg
     * goods_id : 1116
     * goods_price : 99
     * watch_count : 4
     * goods_is_quotable : 1
     * goods_is_brandNew : 0
     * seller_info : weirdo
     */

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
                    ", goods_id=" + goods_id +
                    ", goods_price='" + goods_price + '\'' +
                    ", watch_count=" + watch_count +
                    ", goods_is_quotable=" + goods_is_quotable +
                    ", goods_is_brandNew=" + goods_is_brandNew +
                    ", seller_info='" + seller_info + '\'' +
                    '}';
        }

        private String goods_name;
        private String goods_img_cover;
        private int goods_id;
        private String goods_price;
        private int watch_count;
        private int goods_is_quotable;
        private int goods_is_brandNew;
        private String seller_info;

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

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public int getWatch_count() {
            return watch_count;
        }

        public void setWatch_count(int watch_count) {
            this.watch_count = watch_count;
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

        public String getSeller_info() {
            return seller_info;
        }

        public void setSeller_info(String seller_info) {
            this.seller_info = seller_info;
        }
    }
}
