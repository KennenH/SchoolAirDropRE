package com.example.schoolairdroprefactoredition.model.databean;

import java.util.Calendar;

public class TestQuoteDetailBean {
    private int quoteStatus;
    private String title;
    private int originPrice;
    private int quotePrice;
    private String orderID;// 报价单的唯一标识码
    private String userID;// 用户的唯一标识码
    private Calendar createTime;
    private Calendar expirationTime;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public int getQuoteStatus() {
        return quoteStatus;
    }

    public void setQuoteStatus(int quoteStatus) {
        this.quoteStatus = quoteStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(int originPrice) {
        this.originPrice = originPrice;
    }

    public int getQuotePrice() {
        return quotePrice;
    }

    public void setQuotePrice(int quotePrice) {
        this.quotePrice = quotePrice;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public Calendar getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Calendar expirationTime) {
        this.expirationTime = expirationTime;
    }
}
