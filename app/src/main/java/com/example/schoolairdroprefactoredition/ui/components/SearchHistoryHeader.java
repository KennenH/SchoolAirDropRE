package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

public class SearchHistoryHeader extends ConstraintLayout implements View.OnClickListener {

    private ImageView mDeleteAll;
    private TextView mCancel;
    private TextView mConfirm;
    private FlexboxLayout mFlex;

    private OnHistoryActionListener mOnHistoryActionListener;

    private List<String> mHistories;

    private int startIndex;

    public SearchHistoryHeader(Context context) {
        this(context, null);
    }

    public SearchHistoryHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchHistoryHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_search_hisotry, this, true);

        mDeleteAll = findViewById(R.id.search_history_deleteAll);
        mCancel = findViewById(R.id.search_history_cancel);
        mConfirm = findViewById(R.id.search_history_confirm);
        mFlex = findViewById(R.id.search_history_flex);

        mDeleteAll.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);

        startIndex = getChildCount();
    }

    /**
     * 组合方法
     */
    public void showAfterUpdate(List<String> histories) {
        updateHistories(histories);
        showHistories();
    }

    /**
     * 装填对象的历史记录
     */
    private void updateHistories(List<String> histories) {
        mHistories = histories;
    }

    /**
     * 显示历史记录
     */
    private void showHistories() {
        mFlex.removeAllViews();
        for (String history : mHistories) {
            SearchHistoryItem item = new SearchHistoryItem(getContext(), history.trim());
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = SizeUtils.dp2px(3);
            params.setMargins(margin, margin, margin, margin);
            item.setLayoutParams(params);
            item.setOnClickListener(v -> {
                if (mOnHistoryActionListener != null)
                    mOnHistoryActionListener.onSearchHistory(history.trim());
            });
            mFlex.addView(item);
        }
    }


    public void showDeleteAll() {
        mCancel.setVisibility(GONE);
        mConfirm.setVisibility(GONE);
        mDeleteAll.setVisibility(VISIBLE);
    }

    /**
     * 搜索历史管理回调
     */
    public interface OnHistoryActionListener {
        /**
         * 删除历史记录
         */
        void onDeleteHistory();

        /**
         * 点击搜索历史搜索
         *
         * @param key 搜索关键词
         */
        void onSearchHistory(String key);
    }

    public void setOnHistoryActionListener(OnHistoryActionListener listener) {
        this.mOnHistoryActionListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.search_history_deleteAll) {
            mCancel.setVisibility(VISIBLE);
            mConfirm.setVisibility(VISIBLE);
            mDeleteAll.setVisibility(GONE);
        } else if (id == R.id.search_history_confirm) {
            showDeleteAll();
            if (mOnHistoryActionListener != null) {
                mOnHistoryActionListener.onDeleteHistory();
            }
        } else if (id == R.id.search_history_cancel) {
            showDeleteAll();
        }
    }
}
