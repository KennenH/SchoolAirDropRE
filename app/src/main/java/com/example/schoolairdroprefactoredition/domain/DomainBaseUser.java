package com.example.schoolairdroprefactoredition.domain;

import java.util.List;

import kotlin.jvm.functions.Function2;

/**
 * ！！注意！！
 * 仅在{@link com.example.schoolairdroprefactoredition.repository.UserRepository}
 * 处做临时bean存储器使用
 * ！！注意！！
 */
public class DomainBaseUser {

    /**
     * success : true
     * data : [{"uid":14,"uname":"冲冲Developer","user_img_path":"/assets/user/avatars/14_20201209000844.jpg"}]
     */

    private boolean success;
    /**
     * uid : 14
     * uname : 冲冲Developer
     * user_img_path : /assets/user/avatars/14_20201209000844.jpg
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

    public static class DataBean {
        private int uid;
        private String uname;
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

        public String getUser_img_path() {
            return user_img_path;
        }

        public void setUser_img_path(String user_img_path) {
            this.user_img_path = user_img_path;
        }
    }
}
