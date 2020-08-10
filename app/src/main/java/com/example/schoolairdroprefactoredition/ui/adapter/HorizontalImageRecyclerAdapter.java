package com.example.schoolairdroprefactoredition.ui.adapter;

import android.net.Uri;
import android.os.Build;

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
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)
            image.setImageURI(Uri.fromFile(new File(s.getPath())));
        else image.setImageURI(Uri.fromFile(new File(s.getAndroidQToPath())));
    }


}
