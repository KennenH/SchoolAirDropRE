package com.example.schoolairdroprefactoredition.domain;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class DomainPurchasing implements Serializable {

    @NotNull
    @Override
    public String toString() {
        return "DomainPurchasing{" +
                "code=" + code +
                ", time=" + time +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * success : true
     * data : [{"goods_name":"哈哈哈哈","goods_img_cover":"assets/goods/img/yfmcTw4uFzIkgtXQ_0.jpg","goods_id":1116,"goods_price":99,"watch_count":4,"goods_is_quotable":1,"goods_is_brandNew":0,"seller_info":"weirdo"},{"goods_name":"niko","goods_img_cover":"assets/goods/img/UmMHekJlINW5BYhL_0.jpg","goods_id":1110,"goods_price":1485,"watch_count":10,"goods_is_quotable":1,"goods_is_brandNew":1,"seller_info":"冲冲Developer"},{"goods_name":"在线客服人员的注意","goods_img_cover":"assets/goods/img/HCSVxQvKcpbmkogf_0.jpg","goods_id":1108,"goods_price":120,"watch_count":16,"goods_is_quotable":0,"goods_is_brandNew":0,"seller_info":"冲冲Developer"},{"goods_name":"azazaz","goods_img_cover":"assets/goods/img/6mj9hqYUQpvGSA1M_0.jpg","goods_id":1124,"goods_price":9999,"watch_count":10,"goods_is_quotable":0,"goods_is_brandNew":1,"seller_info":"卢志成sb"},{"goods_name":"低价贱卖二手劳动力","goods_img_cover":"assets/goods/img/759OL4FeobGDtuga_0.jpg","goods_id":1123,"goods_price":10000,"watch_count":3,"goods_is_quotable":0,"goods_is_brandNew":1,"seller_info":"卢志成sb"},{"goods_name":"苏睿是gayking","goods_img_cover":"assets/goods/img/5azm3fFLMr89NqEb_0.jpg","goods_id":1125,"goods_price":100,"watch_count":15,"goods_is_quotable":1,"goods_is_brandNew":0,"seller_info":"clz"},{"goods_name":"哈哈","goods_img_cover":"assets/goods/img/BudvWD7yAlC5SbFz_0.jpg","goods_id":1111,"goods_price":55,"watch_count":3,"goods_is_quotable":1,"goods_is_brandNew":0,"seller_info":"weirdo"},{"goods_name":"嘿嘿","goods_img_cover":"assets/goods/img/ab8Ang6jYLklezJT_0.jpg","goods_id":1113,"goods_price":66,"watch_count":2,"goods_is_quotable":1,"goods_is_brandNew":0,"seller_info":"weirdo"},{"goods_name":"好红红火火恍恍","goods_img_cover":"assets/goods/img/xLzPTa5hYVqwyHkm_0.jpg","goods_id":1126,"goods_price":666,"watch_count":20,"goods_is_quotable":1,"goods_is_brandNew":0,"seller_info":"ikennenx"},{"goods_name":"哈哈哈哈哈哈","goods_img_cover":"assets/goods/img/cghUsZ6qbix42yIv_0.jpg","goods_id":1115,"goods_price":369,"watch_count":8,"goods_is_quotable":1,"goods_is_brandNew":0,"seller_info":"weirdo"},{"goods_name":"这种","goods_img_cover":"assets/goods/img/RvH7nlsdYFjA2coI_0.jpg","goods_id":1094,"goods_price":999,"watch_count":4,"goods_is_quotable":0,"goods_is_brandNew":0,"seller_info":"冲冲Developer"},{"goods_name":"好了","goods_img_cover":"assets/goods/img/qUOzAlDiBnyNI0hG_0.jpg","goods_id":1104,"goods_price":0.05,"watch_count":14,"goods_is_quotable":1,"goods_is_brandNew":1,"seller_info":"冲冲Developer"},{"goods_name":"uuuuuu","goods_img_cover":"assets/goods/img/3ZwWQcuVEyXITnKH_0.jpg","goods_id":1127,"goods_price":99,"watch_count":15,"goods_is_quotable":1,"goods_is_brandNew":0,"seller_info":"ikennenx"},{"goods_name":"我卢本伟没有开挂。","goods_img_cover":"assets/goods/img/fBhaQRund9yc5DO7_0.jpg","goods_id":1114,"goods_price":666.6,"watch_count":7,"goods_is_quotable":1,"goods_is_brandNew":1,"seller_info":"冲冲Developer"},{"goods_name":"roll了roll了","goods_img_cover":"assets/goods/img/VeF0Bxs95h8Lqgry_0.jpg","goods_id":1128,"goods_price":0.001,"watch_count":8,"goods_is_quotable":0,"goods_is_brandNew":0,"seller_info":"冲冲Developer"},{"goods_name":"郑正熊笨蛋","goods_img_cover":"assets/goods/img/MtlLYEOHckPe7hbC_0.jpg","goods_id":1120,"goods_price":0.1,"watch_count":11,"goods_is_quotable":0,"goods_is_brandNew":0,"seller_info":"大帅哥"}]
     */

    private int code;

    private long time;

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        @NotNull
        @Override
        public String toString() {
            return "DataBean{" +
                    "goods_id=" + goods_id +
                    ", goods_name='" + goods_name + '\'' +
                    ", goods_cover_image='" + goods_cover_image + '\'' +
                    ", goods_price='" + goods_price + '\'' +
                    ", goods_is_bargain=" + goods_is_bargain +
                    ", goods_is_secondHand=" + goods_is_secondHand +
                    ", seller=" + seller +
                    '}';
        }

        private int goods_id;
        private String goods_name;
        private String goods_cover_image;
        private String goods_price;
        private String longitude;
        private String latitude;
        private boolean goods_is_bargain;
        private boolean goods_is_secondHand;
        private SellerBean seller;

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public SellerBean getSeller() {
            return seller;
        }

        public void setSeller(SellerBean seller) {
            this.seller = seller;
        }

        public static class SellerBean implements Serializable {

            @NotNull
            @Override
            public String toString() {
                return "SellerBean{" +
                        "user_id=" + user_id +
                        ", user_avatar='" + user_avatar + '\'' +
                        ", user_name='" + user_name + '\'' +
                        '}';
            }

            private int user_id;
            private String user_avatar;
            private String user_name;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getUser_avatar() {
                return user_avatar;
            }

            public void setUser_avatar(String user_avatar) {
                this.user_avatar = user_avatar;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }
        }


        public boolean isGoods_is_secondHand() {
            return goods_is_secondHand;
        }

        public void setGoods_is_secondHand(boolean goods_is_secondHand) {
            this.goods_is_secondHand = goods_is_secondHand;
        }

        public boolean isGoods_is_bargain() {
            return goods_is_bargain;
        }

        public void setGoods_is_bargain(boolean goods_is_bargain) {
            this.goods_is_bargain = goods_is_bargain;
        }


        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_cover_image() {
            return goods_cover_image;
        }

        public void setGoods_cover_image(String goods_cover_image) {
            this.goods_cover_image = goods_cover_image;
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
    }

    /*
     * {
     *     "code": 200,
     *     "msg": null,
     *     "time": "1613487657",
     *     "data": [
     *         {
     *             "goods_id": 8,
     *             "goods_name": "测试物品1",
     *             "goods_cover_image": "/uploads/img/goods/20210129/569505799ba7f2fe82984afe0346da21.png",
     *             "goods_price": 999.99,
     *             "goods_type": "bargin,secondHand",
     *             "user_id": 3,
     *             "user_name": "在线用户测试1",
     *             "user_avatar": "/uploads/img/user/20210129/3b014597dd3c808dce3eb968ae23bbc7.png"
     *         }
     *     ]
     * }
     */
}
