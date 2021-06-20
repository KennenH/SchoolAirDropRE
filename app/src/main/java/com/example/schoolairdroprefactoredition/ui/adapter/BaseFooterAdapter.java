package com.example.schoolairdroprefactoredition.ui.adapter;

import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.ui.components.RecyclerFooter;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseFooterAdapter<T, VH extends BaseViewHolder> extends BaseQuickAdapter<T, VH> {

    private final List<OnNoMoreDataListener> mOnNoMoreDataListener = new ArrayList<>();

    public BaseFooterAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    public void setList(@org.jetbrains.annotations.Nullable Collection<? extends T> list) {
        super.setList(list);
        // 刷新时将之前标记的没有更多数据标志位重置
        for (OnNoMoreDataListener listener : mOnNoMoreDataListener) {
            listener.resetNoMoreData();
        }
        if (list != null && list.size() < ConstantUtil.DEFAULT_PAGE_SIZE) {
            removeAllFooterView();
            addFooter();
        }
    }

    @Override
    public void addData(@NotNull Collection<? extends T> newData) {
        super.addData(newData);
        if (newData.size() < ConstantUtil.DEFAULT_PAGE_SIZE) {
            removeAllFooterView();
            addFooter();
        }
    }

    /**
     * 在没有更多数据时添加没有更多数据提示的尾巴
     */
    private void addFooter() {
        for (OnNoMoreDataListener listener : mOnNoMoreDataListener) {
            listener.onNoMoreData();
        }

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
         * recycler已重新刷新，将没有更多数据标志位重置
         */
        void resetNoMoreData();
    }

    public void addOnNoMoreDataListener(OnNoMoreDataListener listener) {
        mOnNoMoreDataListener.add(listener);
    }

}