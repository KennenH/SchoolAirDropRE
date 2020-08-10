package com.example.schoolairdroprefactoredition.model.databean;

import java.math.BigDecimal;
import java.util.Calendar;

public class TestOnGoingBean {
    private String imgUrl;
    private String title;
    private BigDecimal price;
    private String avatarUrl;
    private String userName;
    private Calendar remaining;

    public Calendar getRemaining() {
        return remaining;
    }

    public void setRemaining(Calendar remaining) {
        this.remaining = remaining;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
