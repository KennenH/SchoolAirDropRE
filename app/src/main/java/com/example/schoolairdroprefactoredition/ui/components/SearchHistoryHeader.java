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

public class SearchHistoryHeader extends ConstraintLayout implements View.OnClickListener {

    private ImageView mDeleteAll;
    private TextView mCancel;
    private TextView mConfirm;
    private FlexboxLayout mFlex;

    private OnDeleteAllListener mOnDeleteAllListener;

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
    }

    public void addHistory(String key) {
        SearchHistoryItem item = new SearchHistoryItem(getContext(), key.trim());
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = SizeUtils.dp2px(3);
        params.setMargins(margin, margin, margin, margin);
        item.setLayoutParams(params);
        mFlex.addView(item);
    }

    public void showDeleteAll() {
        mCancel.setVisibility(GONE);
        mConfirm.setVisibility(GONE);
        mDeleteAll.setVisibility(VISIBLE);
    }

    public interface OnDeleteAllListener {
        /**
         * 确认删除
         */
        void onDeleteConfirm();
    }

    public void setOnDeleteAllListener(OnDeleteAllListener listener) {
        this.mOnDeleteAllListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.search_history_deleteAll) {
            mCancel.setVisibility(VISIBLE);
            mConfirm.setVisibility(VISIBLE);
            mDeleteAll.setVisibility(GONE);
        } else if (id == R.id.search_history_confirm) {
            if (mOnDeleteAllListener != null) {
                mOnDeleteAllListener.onDeleteConfirm();
            }
        } else if (id == R.id.search_history_cancel) {
            showDeleteAll();
        }
    }
}
