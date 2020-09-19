package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.IntDef;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;

public class QuoteStatus extends AppCompatTextView {
    private static final int IC_ACCEPTED = R.drawable.ic_pass;
    private static final int IC_REJECTED = R.drawable.ic_attention_normal;
    private static final int IC_EXPIRED = R.drawable.ic_expired;
    private static final int IC_PENDING = R.drawable.ic_waiting;

    @IntDef({ACCEPTED, REJECTED, EXPIRED, PENDING})
    public @interface Status {
    }

    public static final int ACCEPTED = 101;
    public static final int REJECTED = 111;
    public static final int EXPIRED = 121;
    public static final int PENDING = 131;

    public QuoteStatus(Context context) {
        this(context, null);
    }

    public QuoteStatus(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuoteStatus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int v = SizeUtils.dp2px(context.getResources().getDimension(R.dimen.general_padding_small));
        setCompoundDrawablePadding(v);
        setTextSize(SizeUtils.sp2px(12));
        setGravity(Gravity.CENTER_VERTICAL);
        setPadding(v, v, v, v);
        setVisibility(GONE);
    }

    /**
     * 设置报价当前状态
     *
     * @param status {@link Status}
     */
    public void setStatus(@Status int status) {
        if (status == ACCEPTED) {
            setText(R.string.accepted);
            setCompoundDrawablesWithIntrinsicBounds(IC_ACCEPTED, 0, 0, 0);
        } else if (status == REJECTED) {
            setText(R.string.rejected);
            setCompoundDrawablesWithIntrinsicBounds(IC_REJECTED, 0, 0, 0);
        } else if (status == EXPIRED) {
            setText(R.string.expired);
            setCompoundDrawablesWithIntrinsicBounds(IC_EXPIRED, 0, 0, 0);
        } else if (status == PENDING) {
            setText(R.string.pending);
            setCompoundDrawablesWithIntrinsicBounds(IC_PENDING, 0, 0, 0);
        }
        setVisibility(VISIBLE);
    }
}
