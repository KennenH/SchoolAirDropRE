package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;

public class SSBFilter extends ConstraintLayout {

    private TextView mStatistics;
    private ImageView mFilter;

    private OnFilterListener mOnFilterListener;

    public SSBFilter(Context context) {
        this(context, null);
    }

    public SSBFilter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SSBFilter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_ssb_filter, this, true);

        mStatistics = findViewById(R.id.ssb_header_total);
        mFilter = findViewById(R.id.ssb_header_filter);


    }

    /**
     * 设置当前数据总长度
     */
    public void setDataStatistics(int total) {
        mStatistics.setText(getContext().getString(R.string.statistics, total));
    }

    public interface OnFilterListener {
        void onFilterTimeAsc();

        void onFilterTimeDesc();

        void onFilterWatches();
    }

    public void setOnFilterListener(OnFilterListener listener) {
        this.mOnFilterListener = listener;
    }

}
