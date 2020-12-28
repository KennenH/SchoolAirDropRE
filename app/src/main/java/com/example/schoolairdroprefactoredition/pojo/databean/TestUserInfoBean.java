package com.example.schoolairdroprefactoredition.pojo.databean;

public class TestUserInfoBean {
    // user base info
    private String url;
    private String name;
    private int selling;
    private int sold;
    private int bought;

    // settings
    private String bindingName;
    private boolean isAllowAddViaAlipay = true;
    private boolean isAllowRecommendAlipayFriends = true;
    private boolean isReceiveMessage = true;
    private boolean isReceiveInAppMessage = true;
    private boolean isAllowVibrate = false;
    private boolean isAllowSound = false;

    public TestUserInfoBean() {
        url = "";
        name = "奥里给";
        selling = 3;
        sold = 5;
        bought = 12;
        bindingName = "小鱼子酱";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSelling() {
        return selling;
    }

    public void setSelling(int selling) {
        this.selling = selling;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getBought() {
        return bought;
    }

    public void setBought(int bought) {
        this.bought = bought;
    }

    public String getBindingName() {
        return bindingName;
    }

    public void setBindingName(String bindingName) {
        this.bindingName = bindingName;
    }

    public boolean isAllowAddViaAlipay() {
        return isAllowAddViaAlipay;
    }

    public void setAllowAddViaAlipay(boolean allowAddViaAlipay) {
        isAllowAddViaAlipay = allowAddViaAlipay;
    }

    public boolean isAllowRecommendAlipayFriends() {
        return isAllowRecommendAlipayFriends;
    }

    public void setAllowRecommendAlipayFriends(boolean allowRecommendAlipayFriends) {
        isAllowRecommendAlipayFriends = allowRecommendAlipayFriends;
    }

    public boolean isReceiveMessage() {
        return isReceiveMessage;
    }

    public void setReceiveMessage(boolean receiveMessage) {
        isReceiveMessage = receiveMessage;
    }

    public boolean isReceiveInAppMessage() {
        return isReceiveInAppMessage;
    }

    public void setReceiveInAppMessage(boolean receiveInAppMessage) {
        isReceiveInAppMessage = receiveInAppMessage;
    }

    public boolean isAllowVibrate() {
        return isAllowVibrate;
    }

    public void setAllowVibrate(boolean allowVibrate) {
        isAllowVibrate = allowVibrate;
    }

    public boolean isAllowSound() {
        return isAllowSound;
    }

    public void setAllowSound(boolean allowSound) {
        isAllowSound = allowSound;
    }
}
