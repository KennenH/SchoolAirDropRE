package com.example.schoolairdroprefactoredition.domain;

import java.io.Serializable;
import java.util.List;

public class DomainQuote implements Serializable {

    /**
     * success : true
     * data : [{"quote_id":8,"goods":{"goods_name":"4","goods_img_cover":"assets/goods/img/IpKjir5n8mtXzA01_0.jpg","goods_img_set":"assets/goods/img/IpKjir5n8mtXzA01_1.jpg&assets/goods/img/IpKjir5n8mtXzA01_2.jpg&assets/goods/img/IpKjir5n8mtXzA01_3.jpg&assets/goods/img/IpKjir5n8mtXzA01_4.jpg&assets/goods/img/IpKjir5n8mtXzA01_5.jpg","goods_id":1072,"goods_price":258,"goods_is_quotable":0,"goods_is_brandNew":0,"goods_description":"好像也","goods_sellerid":14},"sender_id":15,"quote_price":"123","state":0,"sender_info":{"uid":15,"uname":"iken","credit_num":2,"user_img_path":"/assets/user/avatars/15_20200923145227.jpg"}},{"quote_id":13,"goods":{"goods_name":"4","goods_img_cover":"assets/goods/img/IpKjir5n8mtXzA01_0.jpg","goods_img_set":"assets/goods/img/IpKjir5n8mtXzA01_1.jpg&assets/goods/img/IpKjir5n8mtXzA01_2.jpg&assets/goods/img/IpKjir5n8mtXzA01_3.jpg&assets/goods/img/IpKjir5n8mtXzA01_4.jpg&assets/goods/img/IpKjir5n8mtXzA01_5.jpg","goods_id":1072,"goods_price":258,"goods_is_quotable":0,"goods_is_brandNew":0,"goods_description":"好像也","goods_sellerid":14},"sender_id":15,"quote_price":"222","state":0,"sender_info":{"uid":15,"uname":"iken","credit_num":2,"user_img_path":"/assets/user/avatars/15_20200923145227.jpg"}},{"quote_id":14,"goods":{"goods_name":"4","goods_img_cover":"assets/goods/img/IpKjir5n8mtXzA01_0.jpg","goods_img_set":"assets/goods/img/IpKjir5n8mtXzA01_1.jpg&assets/goods/img/IpKjir5n8mtXzA01_2.jpg&assets/goods/img/IpKjir5n8mtXzA01_3.jpg&assets/goods/img/IpKjir5n8mtXzA01_4.jpg&assets/goods/img/IpKjir5n8mtXzA01_5.jpg","goods_id":1072,"goods_price":258,"goods_is_quotable":0,"goods_is_brandNew":0,"goods_description":"好像也","goods_sellerid":14},"sender_id":15,"quote_price":"231","state":0,"sender_info":{"uid":15,"uname":"iken","credit_num":2,"user_img_path":"/assets/user/avatars/15_20200923145227.jpg"}},{"quote_id":15,"goods":{"goods_name":"4","goods_img_cover":"assets/goods/img/IpKjir5n8mtXzA01_0.jpg","goods_img_set":"assets/goods/img/IpKjir5n8mtXzA01_1.jpg&assets/goods/img/IpKjir5n8mtXzA01_2.jpg&assets/goods/img/IpKjir5n8mtXzA01_3.jpg&assets/goods/img/IpKjir5n8mtXzA01_4.jpg&assets/goods/img/IpKjir5n8mtXzA01_5.jpg","goods_id":1072,"goods_price":258,"goods_is_quotable":0,"goods_is_brandNew":0,"goods_description":"好像也","goods_sellerid":14},"sender_id":15,"quote_price":"223","state":0,"sender_info":{"uid":15,"uname":"iken","credit_num":2,"user_img_path":"/assets/user/avatars/15_20200923145227.jpg"}},{"quote_id":16,"goods":{"goods_name":"3","goods_img_cover":"assets/goods/img/GjtT2zNbErMcud4I_0.jpg","goods_img_set":"assets/goods/img/GjtT2zNbErMcud4I_1.jpg&assets/goods/img/GjtT2zNbErMcud4I_2.jpg&assets/goods/img/GjtT2zNbErMcud4I_3.jpg&assets/goods/img/GjtT2zNbErMcud4I_4.jpg&assets/goods/img/GjtT2zNbErMcud4I_5.jpg&assets/goods/img/GjtT2zNbErMcud4I_6.jpg&assets/goods/img/GjtT2zNbErMcud4I_7.jpg","goods_id":1075,"goods_price":666,"goods_is_quotable":0,"goods_is_brandNew":0,"goods_description":"a","goods_sellerid":14},"sender_id":15,"quote_price":"666","state":0,"sender_info":{"uid":15,"uname":"iken","credit_num":2,"user_img_path":"/assets/user/avatars/15_20200923145227.jpg"}},{"quote_id":18,"goods":{"goods_name":"我们的生活方式","goods_img_cover":"assets/goods/img/ZJBgkyN8Ebodvwuj_0.jpg","goods_img_set":"","goods_id":1081,"goods_price":8881,"goods_is_quotable":0,"goods_is_brandNew":0,"goods_description":"我在你面前什么都不知道","goods_sellerid":14},"sender_id":15,"quote_price":"666","state":0,"sender_info":{"uid":15,"uname":"iken","credit_num":2,"user_img_path":"/assets/user/avatars/15_20200923145227.jpg"}}]
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
         * quote_id : 8
         * goods : {"goods_name":"4","goods_img_cover":"assets/goods/img/IpKjir5n8mtXzA01_0.jpg","goods_img_set":"assets/goods/img/IpKjir5n8mtXzA01_1.jpg&assets/goods/img/IpKjir5n8mtXzA01_2.jpg&assets/goods/img/IpKjir5n8mtXzA01_3.jpg&assets/goods/img/IpKjir5n8mtXzA01_4.jpg&assets/goods/img/IpKjir5n8mtXzA01_5.jpg","goods_id":1072,"goods_price":258,"goods_is_quotable":0,"goods_is_brandNew":0,"goods_description":"好像也","goods_sellerid":14}
         * sender_id : 15
         * quote_price : 123
         * state : 0
         * sender_info : {"uid":15,"uname":"iken","credit_num":2,"user_img_path":"/assets/user/avatars/15_20200923145227.jpg"}
         */

        private int quote_id;
        private GoodsBean goods;
        private int sender_id;
        private String quote_price;
        private int state;
        private SenderInfoBean sender_info;
        private ReceiverInfoBean receiver_info;

        public ReceiverInfoBean getReceiver_info() {
            return receiver_info;
        }

        public void setReceiver_info(ReceiverInfoBean receiver_info) {
            this.receiver_info = receiver_info;
        }

        public int getQuote_id() {
            return quote_id;
        }

        public void setQuote_id(int quote_id) {
            this.quote_id = quote_id;
        }

        public GoodsBean getGoods() {
            return goods;
        }

        public void setGoods(GoodsBean goods) {
            this.goods = goods;
        }

        public int getSender_id() {
            return sender_id;
        }

        public void setSender_id(int sender_id) {
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

        public static class GoodsBean implements Serializable {
            /**
             * goods_name : 4
             * goods_img_cover : assets/goods/img/IpKjir5n8mtXzA01_0.jpg
             * goods_img_set : assets/goods/img/IpKjir5n8mtXzA01_1.jpg&assets/goods/img/IpKjir5n8mtXzA01_2.jpg&assets/goods/img/IpKjir5n8mtXzA01_3.jpg&assets/goods/img/IpKjir5n8mtXzA01_4.jpg&assets/goods/img/IpKjir5n8mtXzA01_5.jpg
             * goods_id : 1072
             * goods_price : 258
             * goods_is_quotable : 0
             * goods_is_brandNew : 0
             * goods_description : 好像也
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

        public static class SenderInfoBean {
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

        public static class ReceiverInfoBean {
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
