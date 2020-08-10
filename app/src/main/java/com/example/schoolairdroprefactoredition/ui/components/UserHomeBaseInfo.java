package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class UserHomeBaseInfo extends ConstraintLayout implements View.OnClickListener {

    private SimpleDraweeView mAvatar;
    private TextView mUserName;
    private ImageView mVerified;
    private TextView mDescription;
    private TextView mJoinTime;

    private OnBaseInfoActionListener mOnBaseInfoActionListener;

    public UserHomeBaseInfo(Context context) {
        this(context, null);
    }

    public UserHomeBaseInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserHomeBaseInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_user_info, this, true);

        mAvatar = findViewById(R.id.user_avatar);
        mUserName = findViewById(R.id.user_name);
        mVerified = findViewById(R.id.verifyInfo);
        mDescription = findViewById(R.id.user_description);
        mJoinTime = findViewById(R.id.user_join_time);

        mAvatar.setOnClickListener(this);
    }

    public interface OnBaseInfoActionListener {
        void onAvatarClick(ImageView src);
    }

    public void setOnBaseInfoActionListener(OnBaseInfoActionListener listener) {
        this.mOnBaseInfoActionListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.user_avatar) {
            mOnBaseInfoActionListener.onAvatarClick(mAvatar);
        }
    }
}
