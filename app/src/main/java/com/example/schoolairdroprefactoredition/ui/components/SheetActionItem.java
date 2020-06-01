package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;
import com.lihang.ShadowLayout;

public class SheetActionItem extends ConstraintLayout {

    private ImageView mImage;

    public SheetActionItem(Context context) {
        this(context, null);
    }

    public SheetActionItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SheetActionItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_image_text, this, true);

        mImage = findViewById(R.id.item_image);
    }

    /**
     * res must be drawable
     *
     * @param res drawable id
     */
    public void setImageDrawable(Drawable res) {
        mImage.setImageDrawable(res);
    }

    public void setImageBackground(Drawable color) {
        mImage.setBackground(color);
    }
}
