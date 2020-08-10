package com.example.schoolairdroprefactoredition.model.databean;

import java.util.Calendar;

public class TestCreditHistory {
    private int type;
    private int credits;
    private Calendar time;
    private boolean isUp;

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }
}
