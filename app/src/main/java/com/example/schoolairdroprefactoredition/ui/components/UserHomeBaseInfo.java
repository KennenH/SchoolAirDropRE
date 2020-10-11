package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ComponentUserInfoBinding;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.ui.auto.ConstraintLayoutAuto;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

public class UserHomeBaseInfo extends ConstraintLayoutAuto implements View.OnClickListener {

    private ComponentUserInfoBinding binding;

    private OnBaseInfoActionListener mOnBaseInfoActionListener;

    public UserHomeBaseInfo(Context context) {
        this(context, null);
    }

    public UserHomeBaseInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserHomeBaseInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = ComponentUserInfoBinding.bind(LayoutInflater.from(context).inflate(R.layout.component_user_info, this, true));
        binding.userAvatar.setOnClickListener(this);
    }

    public void setUserBaseInfo(DomainUserInfo.DataBean info) {
        try {
            Glide.with(getContext())
                    .load(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + info.getUser_img_path())
                    .circleCrop()
                    .encodeQuality(80)
                    .into(binding.userAvatar);
            binding.userName.setText(info.getUname());
        } catch (NullPointerException e) {
        }
    }

    public void setUserBaseInfo(DomainGoodsInfo.DataBean info) {
        try {
            Glide.with(getContext())
                    .load(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + info.getSeller_info().getUser_img_path())
                    .circleCrop()
                    .encodeQuality(80)
                    .into(binding.userAvatar);
            binding.userName.setText(info.getSeller_info().getUname());
        } catch (NullPointerException e) {
        }
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
            mOnBaseInfoActionListener.onAvatarClick(binding.userAvatar);
        }
    }
}
