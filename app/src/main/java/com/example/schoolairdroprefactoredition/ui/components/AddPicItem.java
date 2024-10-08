package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ItemAddPicBinding;
import com.example.schoolairdroprefactoredition.utils.ImageUtil;

import java.io.File;

public class AddPicItem extends ConstraintLayout implements View.OnClickListener {

    private final ItemAddPicBinding binding;

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
        if (localPath.startsWith("content:") || localPath.startsWith("file:")) {
            binding.image.setImageURI(localPath);
        } else {
            File localFile = new File(localPath);
            binding.image.setImageURI(Uri.fromFile(localFile));
        }
        binding.close.setVisibility(VISIBLE);
    }

    /**
     * 设置图片服务器 全路径 ，带BaseUrl
     */
    public void setImageRemotePath(String pathWithBaseUrl) {
        this.imagePath = pathWithBaseUrl;
        ImageUtil.loadRoundedImage(binding.image, pathWithBaseUrl);
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

        if (isRecoverBackground) {
            binding.image.setImageResource(R.drawable.bg_add_pic);
        }
    }

    public String getImagePath() {
        return this.imagePath;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.image) {
            if (mOnItemAddPicActionListener != null) {
                mOnItemAddPicActionListener.onItemClick();
            }
        } else if (id == R.id.close) {
            if (mOnItemAddPicActionListener != null) {
                mOnItemAddPicActionListener.onClose();
            }
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
