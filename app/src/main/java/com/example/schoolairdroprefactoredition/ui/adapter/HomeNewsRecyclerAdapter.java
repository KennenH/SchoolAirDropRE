package com.example.schoolairdroprefactoredition.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ClickUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.components.BaseHomeNewsEntity;
import com.example.schoolairdroprefactoredition.ui.components.RecyclerFooter;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class HomeNewsRecyclerAdapter extends BaseMultiItemQuickAdapter<BaseHomeNewsEntity, BaseViewHolder> {

    private OnNoMoreDataListener mOnNoMoreDataListener;

    public static final int TYPE_ONE = 0;
    public static final int TYPE_TWO = 1;

    public HomeNewsRecyclerAdapter() {
        addItemType(TYPE_ONE, R.layout.item_home_news_1);
        addItemType(TYPE_TWO, R.layout.item_home_news_2);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, BaseHomeNewsEntity data) {
        if (holder.getItemViewType() == TYPE_ONE) {
            // holder.setImage(news_image,data.getUrl())
            holder.setText(R.id.news_title, data.getTitle());
        } else {
            holder.setText(R.id.news_title, data.getTitle());
        }

        View item = holder.itemView;
        ClickUtils.applyPressedViewScale(item);
        item.setOnClickListener(v -> {
//            GoodsActivity.start(getContext(), new Bundle(), null);
        });
    }

    @Override
    public void setList(@Nullable Collection<? extends BaseHomeNewsEntity> list) {
        super.setList(list);
        if (mOnNoMoreDataListener != null)
            mOnNoMoreDataListener.onNoMoreDataRefresh();

        removeAllFooterView();
        if (list != null && list.size() < ConstantUtil.DATA_FETCH_DEFAULT_SIZE) {
            addFooter();
        }
    }

    @Override
    public void addData(@NotNull Collection<? extends BaseHomeNewsEntity> newData) {
        super.addData(newData);
        if (newData.size() < ConstantUtil.DATA_FETCH_DEFAULT_SIZE) {
            addFooter();
        }
    }

    private void addFooter() {
        if (mOnNoMoreDataListener != null)
            mOnNoMoreDataListener.onNoMoreData();

        RecyclerFooter footer = new RecyclerFooter(getContext());
        footer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addFooterView(footer);
    }

    public interface OnNoMoreDataListener {
        /**
         * 加载的数据已经不足默认数据条数，说明已经没有更多数据
         */
        void onNoMoreData();

        /**
         * recycler已重新刷新，将没有更多数据标志位恢复
         */
        void onNoMoreDataRefresh();
    }

    public void setOnNoMoreDataListener(OnNoMoreDataListener listener) {
        mOnNoMoreDataListener = listener;
    }
}