package com.example.schoolairdroprefactoredition.ui.adapter;

import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.ui.components.RecyclerFooter;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public abstract class BaseFooterAdapter<T, VH extends BaseViewHolder> extends BaseQuickAdapter<T, VH> {

    private OnNoMoreDataListener mOnNoMoreDataListener;

    public BaseFooterAdapter(int layoutResId) {
        super(layoutResId);
    }

    public BaseFooterAdapter(int layoutResId, @org.jetbrains.annotations.Nullable List<T> data) {
        super(layoutResId, data);
    }

    @Override
    public void setList(@org.jetbrains.annotations.Nullable Collection<? extends T> list) {
        super.setList(list);
        if (mOnNoMoreDataListener != null)
            mOnNoMoreDataListener.onNoMoreDataRefresh();

        removeAllFooterView();
        if (list != null && list.size() < ConstantUtil.DATA_FETCH_DEFAULT_SIZE) {
            addNoMoreFooter();
        }
    }

    @Override
    public void addData(@NotNull Collection<? extends T> newData) {
        super.addData(newData);
        removeAllFooterView();
        if (newData.size() < ConstantUtil.DATA_FETCH_DEFAULT_SIZE) {
            addNoMoreFooter();
        }
    }

    /**
     * 在没有更多数据时添加尾巴
     */
    private void addNoMoreFooter() {
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