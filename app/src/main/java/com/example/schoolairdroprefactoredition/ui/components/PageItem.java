package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;

public class PageItem extends ConstraintLayout {

    private ImageView mIconView;
    private TextView mNameView;
    private TextView mDescriptionView;
    private ImageView mArrowView;
    private Switch mSwitch;
    private CheckBox mCheck;

    private boolean isFirst = false;
    private boolean isLast = false;
    private boolean isIconLarge = false;

    private boolean isSwitch = false;
    private boolean isCheck = false;

    private boolean isShowArrow = true;
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
        LayoutInflater.from(context).inflate(R.layout.page_item, this, true);

        initAttrs(context, attrs);
        initView(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.PageItem);
        if (attrs == null)
            return;

        isFirst = attr.getBoolean(R.styleable.PageItem_isFirst, isFirst);
        isLast = attr.getBoolean(R.styleable.PageItem_isLast, isLast);
        isIconLarge = attr.getBoolean(R.styleable.PageItem_isIconLarge, isIconLarge);
        if (isFirst && isLast) {
            isFirst = false;
            isLast = false;
        }

        isSwitch = attr.getBoolean(R.styleable.PageItem_isSwitch, isSwitch);
        isCheck = attr.getBoolean(R.styleable.PageItem_isCheck, isCheck);
        if (isSwitch && isCheck) {
            try {
                throw new Exception("PageItem cannot be set as a Switch and a CheckBox at the same time");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        isShowArrow = attr.getBoolean(R.styleable.PageItem_showArrow, isShowArrow);
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
        mSwitch = findViewById(R.id.item_page_switch);
        mCheck = findViewById(R.id.item_page_check);
        mArrowView = findViewById(R.id.item_page_arrow);

        if (isIconLarge) {
            LayoutParams params = new LayoutParams(SizeUtils.dp2px(60), SizeUtils.dp2px(60));
            mIconView.setLayoutParams(params);
        }

        if (isFirst)
            setBackground(context.getResources().getDrawable(R.drawable.sheet_first, context.getTheme()));
        else if (isLast)
            setBackground(context.getResources().getDrawable(R.drawable.radius_last_button, context.getTheme()));
        else
            setBackground(context.getResources().getDrawable(R.drawable.sheet_button, context.getTheme()));

        if (isSwitch) {
            mArrowView.setVisibility(GONE);
            mCheck.setVisibility(GONE);
            mSwitch.setVisibility(VISIBLE);
        } else if (isCheck) {
            mArrowView.setVisibility(GONE);
            mCheck.setVisibility(VISIBLE);
            mSwitch.setVisibility(GONE);
        } else {
            if (isShowArrow)
                mArrowView.setVisibility(VISIBLE);
            mSwitch.setVisibility(GONE);
            mCheck.setVisibility(GONE);
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

        if (mArrowRes != null && isShowArrow)
            mArrowView.setImageDrawable(mArrowRes);
    }

    public boolean isItemSelected() {
        if (isSwitch)
            return mSwitch.isSelected();
        else if (isCheck)
            return mCheck.isSelected();
        else
            return false;
    }

    public void select() {
        if (isSwitch)
            mSwitch.setChecked(true);
        else if (isCheck)
            mCheck.setSelected(true);
    }

    public void toggle() {
        if (isSwitch)
            mSwitch.toggle();
        else if (isCheck)
            mCheck.toggle();
    }

    public void deSelect() {
        if (isSwitch) {
            mSwitch.setChecked(false);
        } else if (isCheck) {
            mCheck.setSelected(false);
        }
    }
}
