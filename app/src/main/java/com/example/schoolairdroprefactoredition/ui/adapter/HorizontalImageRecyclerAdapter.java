package com.example.schoolairdroprefactoredition.ui.adapter;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.entity.LocalMedia;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class HorizontalImageRecyclerAdapter extends BaseQuickAdapter<LocalMedia, BaseViewHolder> {
    public HorizontalImageRecyclerAdapter() {
        super(R.layout.item_image);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, LocalMedia s) {
        SimpleDraweeView image = (SimpleDraweeView) holder.itemView.getRootView();
        String qPath = s.getAndroidQToPath();
        image.setImageURI(Uri.fromFile(new File(qPath == null ? s.getPath() : qPath)));
    }
}
