package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;

public class PageItem extends ConstraintLayout {

    private ImageView mIconView;
    private TextView mNameView;
    private TextView mDescriptionView;
    private ImageView mArrowView;
    private Switch mSwitch;

    private boolean isFirst = false;
    private boolean isLast = false;
    private boolean isSwitch = false;
    private String mName = "";
    private String mDescription = "";
    private Drawable mIconRes;
    private Drawable mArrowRes;

    public PageItem(Context context) {
        this(context, null);
    }

    public PageItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_page, this, true);

        initAttrs(context, attrs);
        initView(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.PageItem);
        if (attrs == null)
            return;

        isFirst = attr.getBoolean(R.styleable.PageItem_isFirst, isFirst);
        isLast = attr.getBoolean(R.styleable.PageItem_isLast, isLast);
        isSwitch = attr.getBoolean(R.styleable.PageItem_isSwitch, isSwitch);
        mName = attr.getString(R.styleable.PageItem_title);
        mDescription = attr.getString(R.styleable.PageItem_description);
        mIconRes = attr.getDrawable(R.styleable.PageItem_icon);
        mArrowRes = attr.getDrawable(R.styleable.PageItem_arrow);

        attr.recycle();
    }

    private void initView(Context context) {
        mIconView = findViewById(R.id.item_page_icon);
        mNameView = findViewById(R.id.item_page_name);
        mDescriptionView = findViewById(R.id.item_page_description);
        mSwitch = findViewById(R.id.item_page_switched);
        mArrowView = findViewById(R.id.item_page_arrow);

        if (isFirst)
            setBackground(context.getResources().getDrawable(R.drawable.sheet_first, context.getTheme()));
        else if (isLast)
            setBackground(context.getResources().getDrawable(R.drawable.radius_last_button, context.getTheme()));
        else
            setBackground(context.getResources().getDrawable(R.drawable.sheet_button, context.getTheme()));

        if (isSwitch) {
            mArrowView.setVisibility(GONE);
            mSwitch.setVisibility(VISIBLE);
        } else {
            mArrowView.setVisibility(VISIBLE);
            mSwitch.setVisibility(GONE);
        }

        if (mDescription != null && !mDescription.trim().equals("")) {
            mDescriptionView.setVisibility(VISIBLE);
            mDescriptionView.setText(mDescription);
        } else
            mDescriptionView.setVisibility(GONE);

        if (mIconRes != null)
            mIconView.setImageDrawable(mIconRes);

        if (mName != null)
            mNameView.setText(mName);

        if (mArrowRes != null)
            mArrowView.setImageDrawable(mArrowRes);
    }

    public boolean isSelected() {
        return mSwitch.isSelected();
    }

    public void select() {
        mSwitch.setChecked(true);
    }
}
