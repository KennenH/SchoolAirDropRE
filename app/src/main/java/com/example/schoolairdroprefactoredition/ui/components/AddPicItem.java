package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ItemAddPicBinding;
import com.example.schoolairdroprefactoredition.utils.ImageUtil;

import java.io.File;

public class AddPicItem extends ConstraintLayout implements View.OnClickListener {

    private ItemAddPicBinding binding;

    private OnItemAddPicActionListener mOnItemAddPicActionListener;

    private String imagePath;

    public AddPicItem(@NonNull Context context) {
        this(context, null);
    }

    public AddPicItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddPicItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AddPicItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        binding = ItemAddPicBinding.bind(LayoutInflater.from(context).inflate(R.layout.item_add_pic, this, true));

        binding.image.setOnClickListener(this);
        binding.close.setOnClickListener(this);
        binding.close.setVisibility(INVISIBLE);
        binding.image.setImageResource(R.drawable.bg_add_pic);
    }

    /**
     * 设置图片本地路径
     */
    public void setImageLocalPath(String localPath) {
        this.imagePath = localPath;
        binding.image.setImageURI(Uri.fromFile(new File(localPath)));
        binding.close.setVisibility(VISIBLE);
    }

    /**
     * 设置图片服务器 全路径 ，带BaseUrl
     */
    public void setImageRemotePath(String pathWithBaseUrl) {
        this.imagePath = pathWithBaseUrl;
        ImageUtil.loadRoundedImage(binding.image, pathWithBaseUrl, SizeUtils.dp2px(40), SizeUtils.dp2px(40));
        binding.close.setVisibility(VISIBLE);
    }

    /**
     * 清除图片
     *
     * @param isRecoverBackground 是否在删除图片后恢复背景，封面需要调用
     *                            图片集直接删除而无需恢复背景
     */
    public void clearImage(boolean isRecoverBackground) {
        imagePath = "";
        binding.close.setVisibility(INVISIBLE);

        if (isRecoverBackground)
            binding.image.setImageResource(R.drawable.bg_add_pic);
    }

    public String getImagePath() {
        return this.imagePath;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                if (mOnItemAddPicActionListener != null)
                    mOnItemAddPicActionListener.onItemClick();
                break;
            case R.id.close:
                if (mOnItemAddPicActionListener != null)
                    mOnItemAddPicActionListener.onClose();
                break;
            default:
                break;
        }
    }

    public interface OnItemAddPicActionListener {
        /**
         * 点击关闭按钮
         */
        void onClose();

        /**
         * 点击图片
         */
        void onItemClick();
    }

    public void setOnItemAddPicActionListener(OnItemAddPicActionListener listener) {
        mOnItemAddPicActionListener = listener;
    }
}
