package com.example.schoolairdroprefactoredition.domain;

import java.io.Serializable;

/**
 * 用户基础信息bean
 */
public class DomainBaseUserInfo implements Serializable {

    /**
     * uid : 15
     * uname : user_15
     * user_img_path : http://106.54.110.46/public/assets/user/avatars/default.jpg
     */


    private int uid;
    private String uname;
    private String user_img_path;
    private int count_on_sale;

    public int getCount_on_sale() {
        return count_on_sale;
    }

    public void setCount_on_sale(int count_on_sale) {
        this.count_on_sale = count_on_sale;
    }

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

    public String getUser_img_path() {
        return user_img_path;
    }

    public void setUser_img_path(String user_img_path) {
        this.user_img_path = user_img_path;
    }
}
