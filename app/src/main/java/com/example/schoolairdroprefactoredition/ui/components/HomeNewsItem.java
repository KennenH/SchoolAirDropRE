package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class HomeNewsItem extends ConstraintLayout {

    private TextView mDay;
    private TextView mMonth;
    private TextView mTitle;
    private TextView mSender;
    private SimpleDraweeView mCover;

    public HomeNewsItem(Context context) {
        this(context, null);
    }

    public HomeNewsItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeNewsItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_home_news, this, true);

        mDay = findViewById(R.id.news_day);
        mMonth = findViewById(R.id.news_month);
        mTitle = findViewById(R.id.news_title);
        mSender = findViewById(R.id.news_sender);
        mCover = findViewById(R.id.news_cover);


    }

    public void setDay(String day) {
        mDay.setText(day);
    }

    public void setMonth(String day) {
        mMonth.setText(day);
    }

    public void setTitle(String day) {
        mTitle.setText(day);
    }

    public void setSender(String day) {
        mSender.setText(day);
    }

    public void setCover(String url) {
        mCover.setImageURI(url);
    }

    public String getDay() {
        return mDay.getText().toString();
    }

    public String getMonth() {
        return mMonth.getText().toString();
    }

    public String getTitle() {
        return mTitle.getText().toString();
    }

    public String getSender() {
        return mSender.getText().toString();
    }
}
