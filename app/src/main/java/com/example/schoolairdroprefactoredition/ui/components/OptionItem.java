package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.SwitchCompat;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.auto.ConstraintLayoutAuto;

/**
 * 有两个选项，一个是可以带箭头的点击设置的选项按钮，一个是带开关的可以开启关闭的选项按钮
 */
public class OptionItem extends ConstraintLayoutAuto {

    private final TextView mTitle;
    private final TextView mDescription;
    private final ImageView mArrow;
    private final SwitchCompat mSwitch;
    private final ImageView mImage;

    /**
     * 是否是开关，默认否
     */
    private boolean isSwitch = false;

    /**
     * 若为开关，是否显示被打开状态，默认否
     */
    private boolean isCheck = false;

    /**
     * 是否显示尾部的右箭头，默认是
     */
    private boolean isShowArrow = false;

    /**
     * 是否有图片显示，默认否
     */
    private boolean hasImage = false;

    private String title = "";
    private String description = "";

    public OptionItem(Context context) {
        this(context, null);
    }

    public OptionItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OptionItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_selling_add_new, this, true);

        mTitle = findViewById(R.id.title);
        mDescription = findViewById(R.id.description);
        mArrow = findViewById(R.id.arrow);
        mSwitch = findViewById(R.id.swi);
        mImage = findViewById(R.id.image);

        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.OptionItem);
        if (attrs == null) return;

        isCheck = ta.getBoolean(R.styleable.OptionItem_SO_checked, isCheck);
        isSwitch = ta.getBoolean(R.styleable.OptionItem_SO_isSwitch, isSwitch);
        isShowArrow = ta.getBoolean(R.styleable.OptionItem_SO_isShowArrow, isShowArrow);
        hasImage = ta.getBoolean(R.styleable.OptionItem_SO_hasImage, hasImage);
        title = ta.getString(R.styleable.OptionItem_SO_title);
        description = ta.getString(R.styleable.OptionItem_SO_description);

        ta.recycle();
    }

    private void init() {
        if (isSwitch) { // 开关类型
            mSwitch.setChecked(isCheck);
            mSwitch.setVisibility(VISIBLE);
            mDescription.setVisibility(GONE);
            mArrow.setVisibility(INVISIBLE);
        } else { // 不是开关
            mSwitch.setVisibility(INVISIBLE);
            mDescription.setVisibility(VISIBLE);
            mArrow.setVisibility(VISIBLE);
        }

        if (hasImage) {
            mImage.setVisibility(VISIBLE);
        } else {
            mImage.setVisibility(GONE);
        }

        if (isShowArrow) {
            mSwitch.setVisibility(INVISIBLE);
            mArrow.setVisibility(VISIBLE);
        }

        if (title != null && !title.equals("")) mTitle.setText(title);
        if (description != null && !description.equals("")) {
            mDescription.setText(description);
        }
    }

    /**
     * 在hasImage下设置纯颜色资源
     */
    public void setDrawable(@DrawableRes int color) {
        mImage.setBackgroundResource(color);
    }

    /**
     * 在hasImage下获取image view，便于外部使用view加载图片
     */
    public ImageView getImageView() {
        return mImage;
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
