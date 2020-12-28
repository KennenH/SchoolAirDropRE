package com.example.schoolairdroprefactoredition.pojo.databean;

import com.example.schoolairdroprefactoredition.ui.components.BaseHomeNewsEntity;

public class TestNewsItemBean extends BaseHomeNewsEntity {
    private String month;
    private String day;
    private String sender;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
