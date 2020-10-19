package com.example.schoolairdroprefactoredition.domain.base;

import java.io.Serializable;

/**
 * 用户基础信息bean
 */
public class DomainBaseUserInfo implements Serializable {
    @Override
    public String toString() {
        return "DomainBaseUserInfo{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", user_img_path='" + user_img_path + '\'' +
                ", credit_num=" + credit_num +
                '}';
    }

    /**
     * uid : 15
     * uname : user_15
     * user_img_path : http://106.54.110.46/public/assets/user/avatars/default.jpg
     * credit_num : 2
     */


    private int uid;
    private String uname;
    private String user_img_path;
    private int credit_num;

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

    public int getCredit_num() {
        return credit_num;
    }

    public void setCredit_num(int credit_num) {
        this.credit_num = credit_num;
    }
}
