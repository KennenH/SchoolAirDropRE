package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;

public class SellingOption extends ConstraintLayout {

    private TextView mTitle;
    private TextView mDescription;
    private Switch mSwitch;
    private ImageView mArrow;

    private boolean isSwitch = false;
    private boolean clickable = false;
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

        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SellingOption);
        if (attrs == null) return;

        isSwitch = ta.getBoolean(R.styleable.SellingOption_SO_isSwitch, isSwitch);
        clickable = ta.getBoolean(R.styleable.SellingOption_SO_clickable, clickable);
        title = ta.getString(R.styleable.SellingOption_SO_title);
        description = ta.getString(R.styleable.SellingOption_SO_description);

        ta.recycle();
    }

    private void init() {
        mTitle = findViewById(R.id.title);
        mDescription = findViewById(R.id.description);
        mArrow = findViewById(R.id.arrow);
        mSwitch = findViewById(R.id.swi);

        if (isSwitch) {
            mSwitch.setVisibility(VISIBLE);
            mDescription.setVisibility(GONE);
            mArrow.setVisibility(INVISIBLE);
        } else {
            mSwitch.setVisibility(GONE);
            mDescription.setVisibility(VISIBLE);
            mArrow.setVisibility(VISIBLE);
        }

        if (clickable) {
            mSwitch.setVisibility(GONE);
            mArrow.setVisibility(VISIBLE);
        }

        if (title != null && !title.equals("")) mTitle.setText(title);
        if (description != null && !description.equals("")) mDescription.setText(description);

    }
}
