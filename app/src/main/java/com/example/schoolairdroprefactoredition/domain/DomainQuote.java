package com.example.schoolairdroprefactoredition.domain;

import java.io.Serializable;
import java.util.List;

public class DomainQuote implements Serializable {
    /**
     * success : true
     * data : [{"quote_id":"1602819155430","goods":{"goods_name":"1","goods_img_cover":"assets/goods/img/tm8dpPJygBL20kXE_0.jpg","goods_img_set":"assets/goods/img/tm8dpPJygBL20kXE_1.jpg&assets/goods/img/tm8dpPJygBL20kXE_2.jpg&assets/goods/img/tm8dpPJygBL20kXE_3.jpg&assets/goods/img/tm8dpPJygBL20kXE_4.jpg&assets/goods/img/tm8dpPJygBL20kXE_5.jpg","goods_id":1076,"goods_price":999,"goods_is_quotable":1,"goods_is_brandNew":0,"goods_description":"啊啊啊","goods_sellerid":14},"sender_id":null,"quote_price":"55","state":1,"sender_info":null,"receiver_info":{"uid":14,"uname":"lucky熊zz","credit_num":2,"user_img_path":"/assets/user/avatars/14_20201020152934.jpg"}},{"quote_id":"1602735505280","goods":{"goods_name":"月亮圆","goods_img_cover":"assets/goods/img/IdGSQr1pohtkfA2E_0.jpg","goods_img_set":"","goods_id":1061,"goods_price":120,"goods_is_quotable":1,"goods_is_brandNew":1,"goods_description":"哈哈哈哈睡觉睡觉睡觉睡觉睡觉","goods_sellerid":14},"sender_id":null,"quote_price":"6666","state":2,"sender_info":null,"receiver_info":{"uid":14,"uname":"lucky熊zz","credit_num":2,"user_img_path":"/assets/user/avatars/14_20201020152934.jpg"}},{"quote_id":"1602735510969","goods":{"goods_name":"月亮圆","goods_img_cover":"assets/goods/img/IdGSQr1pohtkfA2E_0.jpg","goods_img_set":"","goods_id":1061,"goods_price":120,"goods_is_quotable":1,"goods_is_brandNew":1,"goods_description":"哈哈哈哈睡觉睡觉睡觉睡觉睡觉","goods_sellerid":14},"sender_id":null,"quote_price":"6666","state":2,"sender_info":null,"receiver_info":{"uid":14,"uname":"lucky熊zz","credit_num":2,"user_img_path":"/assets/user/avatars/14_20201020152934.jpg"}},{"quote_id":"1602735668879","goods":{"goods_name":"月亮圆","goods_img_cover":"assets/goods/img/IdGSQr1pohtkfA2E_0.jpg","goods_img_set":"","goods_id":1061,"goods_price":120,"goods_is_quotable":1,"goods_is_brandNew":1,"goods_description":"哈哈哈哈睡觉睡觉睡觉睡觉睡觉","goods_sellerid":14},"sender_id":null,"quote_price":"6666","state":1,"sender_info":null,"receiver_info":{"uid":14,"uname":"lucky熊zz","credit_num":2,"user_img_path":"/assets/user/avatars/14_20201020152934.jpg"}},{"quote_id":"1603163055999","goods":{"goods_name":"月亮圆","goods_img_cover":"assets/goods/img/IdGSQr1pohtkfA2E_0.jpg","goods_img_set":"","goods_id":1061,"goods_price":120,"goods_is_quotable":1,"goods_is_brandNew":1,"goods_description":"哈哈哈哈睡觉睡觉睡觉睡觉睡觉","goods_sellerid":14},"sender_id":null,"quote_price":"666","state":2,"sender_info":null,"receiver_info":{"uid":14,"uname":"lucky熊zz","credit_num":2,"user_img_path":"/assets/user/avatars/14_20201020152934.jpg"}}]
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
        /**
         * quote_id : 1602819155430
         * goods : {"goods_name":"1","goods_img_cover":"assets/goods/img/tm8dpPJygBL20kXE_0.jpg","goods_img_set":"assets/goods/img/tm8dpPJygBL20kXE_1.jpg&assets/goods/img/tm8dpPJygBL20kXE_2.jpg&assets/goods/img/tm8dpPJygBL20kXE_3.jpg&assets/goods/img/tm8dpPJygBL20kXE_4.jpg&assets/goods/img/tm8dpPJygBL20kXE_5.jpg","goods_id":1076,"goods_price":999,"goods_is_quotable":1,"goods_is_brandNew":0,"goods_description":"啊啊啊","goods_sellerid":14}
         * sender_id : null
         * quote_price : 55
         * state : 1
         * sender_info : null
         * receiver_info : {"uid":14,"uname":"lucky熊zz","credit_num":2,"user_img_path":"/assets/user/avatars/14_20201020152934.jpg"}
         */

        private String quote_id;
        private GoodsBean goods;
        private String sender_id;
        private String quote_price;
        private int state;
        private ReceiverInfoBean receiver_info;
        private SenderInfoBean sender_info;

        public String getQuote_id() {
            return quote_id;
        }

        public void setQuote_id(String quote_id) {
            this.quote_id = quote_id;
        }

        public GoodsBean getGoods() {
            return goods;
        }

        public void setGoods(GoodsBean goods) {
            this.goods = goods;
        }

        public Object getSender_id() {
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

        public SenderInfoBean getSender_info() {
            return sender_info;
        }

        public void setSender_info(SenderInfoBean sender_info) {
            this.sender_info = sender_info;
        }

        public ReceiverInfoBean getReceiver_info() {
            return receiver_info;
        }

        public void setReceiver_info(ReceiverInfoBean receiver_info) {
            this.receiver_info = receiver_info;
        }

        public static class GoodsBean implements Serializable {
            /**
             * goods_name : 1
             * goods_img_cover : assets/goods/img/tm8dpPJygBL20kXE_0.jpg
             * goods_img_set : assets/goods/img/tm8dpPJygBL20kXE_1.jpg&assets/goods/img/tm8dpPJygBL20kXE_2.jpg&assets/goods/img/tm8dpPJygBL20kXE_3.jpg&assets/goods/img/tm8dpPJygBL20kXE_4.jpg&assets/goods/img/tm8dpPJygBL20kXE_5.jpg
             * goods_id : 1076
             * goods_price : 999
             * goods_is_quotable : 1
             * goods_is_brandNew : 0
             * goods_description : 啊啊啊
             * goods_sellerid : 14
             */

            private String goods_name;
            private String goods_img_cover;
            private String goods_img_set;
            private int goods_id;
            private String goods_price;
            private int goods_is_quotable;
            private int goods_is_brandNew;
            private String goods_description;
            private int goods_sellerid;

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

            public int getGoods_sellerid() {
                return goods_sellerid;
            }

            public void setGoods_sellerid(int goods_sellerid) {
                this.goods_sellerid = goods_sellerid;
            }
        }

        public static class SenderInfoBean implements Serializable {
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


        public static class ReceiverInfoBean implements Serializable {
            /**
             * uid : 14
             * uname : lucky熊zz
             * credit_num : 2
             * user_img_path : /assets/user/avatars/14_20201020152934.jpg
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
