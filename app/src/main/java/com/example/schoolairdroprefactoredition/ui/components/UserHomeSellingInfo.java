package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ComponentUserMoreInfoBinding;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.ui.auto.ConstraintLayoutAuto;

public class UserHomeSellingInfo extends ConstraintLayoutAuto implements View.OnClickListener {

    private static final int CREDIT1 = R.string.ic_credit1;
    private static final int CREDIT2 = R.string.ic_credit2;
    private static final int CREDIT3 = R.string.ic_credit3;
    private static final int CREDIT4 = R.string.ic_credit4;
    private static final int CREDIT5 = R.string.ic_credit5;

    private ComponentUserMoreInfoBinding binding;

    private OnMoreInfoActionListener mOnMoreInfoActionListener;

    public UserHomeSellingInfo(Context context) {
        this(context, null);
    }

    public UserHomeSellingInfo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserHomeSellingInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = ComponentUserMoreInfoBinding.bind(LayoutInflater.from(context).inflate(R.layout.component_user_more_info, this, true));

        binding.userMoreSelling.setOnClickListener(this);
        binding.userMoreCredits.setOnClickListener(this);
    }

    public void setUserMoreInfo(DomainUserInfo.DataBean info) {
        binding.userMoreSelling.setText(String.valueOf(info.getSelling()));
        switch (info.getCredit_num()) {
            case 1:
                binding.userMoreCredits.setText(CREDIT1);
                break;
            case 2:
                binding.userMoreCredits.setText(CREDIT2);
                break;
            case 3:
                binding.userMoreCredits.setText(CREDIT3);
                break;
            case 4:
                binding.userMoreCredits.setText(CREDIT4);
                break;
            case 5:
                binding.userMoreCredits.setText(CREDIT5);
                break;
        }
    }

    public void setUserMoreInfo(DomainGoodsInfo.DataBean info) {
        binding.userMoreSelling.setText(R.string.sellingHideByUser); // todo 用户应该可以查看其他用户在售物品的个数
        switch (info.getSeller_info().getCredit_num()) {
            case 1:
                binding.userMoreCredits.setText(CREDIT1);
                break;
            case 2:
                binding.userMoreCredits.setText(CREDIT2);
                break;
            case 3:
                binding.userMoreCredits.setText(CREDIT3);
                break;
            case 4:
                binding.userMoreCredits.setText(CREDIT4);
                break;
            case 5:
                binding.userMoreCredits.setText(CREDIT5);
                break;
        }
    }

    public interface OnMoreInfoActionListener {
        void onSellingClick();

        void onCreditsClick();
    }

    public void setOnMoreInfoActionListener(OnMoreInfoActionListener listener) {
        mOnMoreInfoActionListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.user_more_selling)
            mOnMoreInfoActionListener.onSellingClick();
        else if (id == R.id.user_more_credits)
            mOnMoreInfoActionListener.onCreditsClick();
    }
}
