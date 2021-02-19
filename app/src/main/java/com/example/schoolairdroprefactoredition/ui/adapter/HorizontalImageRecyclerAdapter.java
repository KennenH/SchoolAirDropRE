package com.example.schoolairdroprefactoredition.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.components.AddPicItem;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.luck.picture.lib.entity.LocalMedia;

import org.jetbrains.annotations.NotNull;

public class HorizontalImageRecyclerAdapter extends BaseQuickAdapter<LocalMedia, BaseViewHolder> {

    private OnPicSetClickListener mOnPicSetClickListener;

    public HorizontalImageRecyclerAdapter() {
        super(R.layout.item_image);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, LocalMedia s) {
        AddPicItem image = (AddPicItem) holder.itemView.getRootView();
        String realPath = s.getAndroidQToPath() == null ? s.getPath() : s.getAndroidQToPath();

        // 检查图片路径是否以base url开头
        if (realPath.startsWith(ConstantUtil.QINIU_BASE_URL)) {
            image.setImageRemotePath(realPath); // 若是则为服务器图片
        } else {
            image.setImageLocalPath(realPath); // 否则为本地图片
        }

        image.setOnItemAddPicActionListener(new AddPicItem.OnItemAddPicActionListener() {
            @Override
            public void onClose() {
                if (mOnPicSetClickListener != null)
                    mOnPicSetClickListener.onPicSetDeleteAt(getItemPosition(s));

                image.clearImage(false);
                remove(s);
            }

            @Override
            public void onItemClick() {
                if (mOnPicSetClickListener != null)
                    mOnPicSetClickListener.onPicSetClick(holder.itemView.findViewById(R.id.image), getItemPosition(s));
            }
        });
    }

    public interface OnPicSetClickListener {
        /**
         * 图片集的一张图片被删除
         */
        void onPicSetDeleteAt(int pos);

        /**
         * 图片集被点击
         *
         * @param pos 点击的位置
         */
        void onPicSetClick(ImageView source, int pos);
    }

    public void setOnPicSetClickListener(OnPicSetClickListener listener) {
        mOnPicSetClickListener = listener;
    }

}
