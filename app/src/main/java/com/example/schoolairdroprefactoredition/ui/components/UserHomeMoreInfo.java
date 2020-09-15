package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;

public class UserHomeMoreInfo extends ConstraintLayout implements View.OnClickListener {

    private static final int CREDIT1 = R.drawable.ic_credit1;
    private static final int CREDIT2 = R.drawable.ic_credit2;
    private static final int CREDIT3 = R.drawable.ic_credit3;
    private static final int CREDIT4 = R.drawable.ic_credit4;
    private static final int CREDIT5 = R.drawable.ic_credit5;

    private TextView mTransactions;
    private ImageView mCredits;

    private OnMoreInfoActionListener mOnMoreInfoActionListener;

    public UserHomeMoreInfo(Context context) {
        this(context, null);
    }

    public UserHomeMoreInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserHomeMoreInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_user_more_info, this, true);

        mTransactions = findViewById(R.id.user_more_transactions);
        mCredits = findViewById(R.id.user_more_credits);

        mTransactions.setOnClickListener(this);
        mCredits.setOnClickListener(this);
    }

    public void setUserMoreInfo(DomainUserInfo.DataBean info) {
        switch (info.getCredit_num()) {
            case 1:
                mCredits.setImageResource(CREDIT1);
                break;
            case 2:
                mCredits.setImageResource(CREDIT2);
                break;
            case 3:
                mCredits.setImageResource(CREDIT3);
                break;
            case 4:
                mCredits.setImageResource(CREDIT4);
                break;
            case 5:
                mCredits.setImageResource(CREDIT5);
                break;
        }
    }

    public interface OnMoreInfoActionListener {
        void onTransactionClick();

        void onCreditsClick();
    }

    public void setOnMoreInfoActionListener(OnMoreInfoActionListener listener) {
        mOnMoreInfoActionListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.user_more_transactions)
            mOnMoreInfoActionListener.onTransactionClick();
        else if (id == R.id.user_more_credits)
            mOnMoreInfoActionListener.onCreditsClick();
    }
}
