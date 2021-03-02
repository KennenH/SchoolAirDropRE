package com.example.schoolairdroprefactoredition.domain;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class GoodsDetailInfo implements Serializable {

    @NotNull
    @Override
    public String toString() {
        return "GoodsDetailInfo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", time=" + time +
                ", data=" + data +
                '}';
    }

    private int code;
    private String msg;
    private long time;

    private boolean isFavorite;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

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

    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
                    "goods_images='" + goods_images + '\'' +
                    ", goods_content='" + goods_content + '\'' +
                    ", goods_watch_count=" + goods_watch_count +
                    ", goods_favor_count=" + goods_favor_count +
                    ", goods_chat_count=" + goods_chat_count +
                    '}';
        }

        private String goods_images;
        private String goods_content;
        private int goods_watch_count;
        private int goods_favor_count;
        private int goods_chat_count;
        private SellerBean seller;

        public SellerBean getSeller() {
            return seller;
        }

        public void setSeller(SellerBean seller) {
            this.seller = seller;
        }

        public static class SellerBean implements Serializable {
            @Override
            public String toString() {
                return "SellerBean{" +
                        "last_login_time='" + last_login_time + '\'' +
                        '}';
            }

            private String last_login_time;

            public String getLast_login_time() {
                return last_login_time;
            }

            public void setLast_login_time(String last_login_time) {
                this.last_login_time = last_login_time;
            }
        }


        public int getGoods_favor_count() {
            return goods_favor_count;
        }

        public void setGoods_favor_count(int goods_favor_count) {
            this.goods_favor_count = goods_favor_count;
        }

        public int getGoods_chat_count() {
            return goods_chat_count;
        }

        public void setGoods_chat_count(int goods_chat_count) {
            this.goods_chat_count = goods_chat_count;
        }

        public int getGoods_watch_count() {
            return goods_watch_count;
        }

        public void setGoods_watch_count(int goods_watch_count) {
            this.goods_watch_count = goods_watch_count;
        }

        public String getGoods_images() {
            return goods_images;
        }

        public void setGoods_images(String goods_images) {
            this.goods_images = goods_images;
        }

        public String getGoods_content() {
            return goods_content;
        }

        public void setGoods_content(String goods_content) {
            this.goods_content = goods_content;
        }
    }
}
