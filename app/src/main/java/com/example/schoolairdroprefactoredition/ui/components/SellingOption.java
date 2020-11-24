package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.auto.ConstraintLayoutAuto;

public class SellingOption extends ConstraintLayoutAuto {

    private final TextView mTitle;
    private final TextView mDescription;
    private final ImageView mArrow;
    private final SwitchCompat mSwitch;

    private boolean isSwitch = false;
    private boolean isShowArrow = false;
    private String title = "";
    private String description = "";

    public SellingOption(Context context) {
        this(context, null);
    }

    public SellingOption(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SellingOption(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_selling_add_new, this, true);

        mTitle = findViewById(R.id.title);
        mDescription = findViewById(R.id.description);
        mArrow = findViewById(R.id.arrow);
        mSwitch = findViewById(R.id.swi);

        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SellingOption);
        if (attrs == null) return;

        isSwitch = ta.getBoolean(R.styleable.SellingOption_SO_isSwitch, isSwitch);
        isShowArrow = ta.getBoolean(R.styleable.SellingOption_SO_clickable, isShowArrow);
        title = ta.getString(R.styleable.SellingOption_SO_title);
        description = ta.getString(R.styleable.SellingOption_SO_description);

        ta.recycle();
    }

    private void init() {
        if (isSwitch) {
            mSwitch.setVisibility(VISIBLE);
            mDescription.setVisibility(GONE);
            mArrow.setVisibility(INVISIBLE);
        } else {
            mSwitch.setVisibility(INVISIBLE);
            mDescription.setVisibility(VISIBLE);
            mArrow.setVisibility(VISIBLE);
        }

        if (isShowArrow) {
            mSwitch.setVisibility(INVISIBLE);
            mArrow.setVisibility(VISIBLE);
        }

        if (title != null && !title.equals("")) mTitle.setText(title);
        if (description != null && !description.equals(""))
            mDescription.setText(description);
    }

    public CharSequence getText() {
        return mTitle.getText();
    }

    public CharSequence getDescription() {
        return mDescription.getText();
    }

    public void setText(CharSequence text) {
        mTitle.setText(text);
    }

    public void setDescription(CharSequence description) {
        mDescription.setText(description);
    }

    public boolean getIsChecked() {
        return mSwitch.isChecked();
    }

    public void toggle() {
        mSwitch.toggle();
    }
}
