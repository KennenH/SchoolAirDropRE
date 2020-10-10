package com.example.schoolairdroprefactoredition.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.scene.ssb.fragment.BoughtFragment;
import com.example.schoolairdroprefactoredition.scene.ssb.fragment.SSBBaseFragment;
import com.example.schoolairdroprefactoredition.scene.ssb.fragment.SellingFragment;
import com.example.schoolairdroprefactoredition.scene.ssb.fragment.SoldFragment;

public class SSBPagerAdapter extends FragmentPagerAdapter implements SSBBaseFragment.OnSSBDataLenChangedListener {

    private Context mContext;
    private OnSSBDataLenChangedIntermediaryListener mOnSSBDataLenChangedIntermediaryListener;

    public SSBPagerAdapter(Context context, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case SSBBaseFragment.SELLING_POS:
                SellingFragment fragmentSelling = SellingFragment.newInstance();
                fragmentSelling.setOnSSBDataLenChangedListener(this);
                return fragmentSelling;
            case SSBBaseFragment.SOLD_POS:
                SoldFragment fragmentSold = SoldFragment.newInstance();
                fragmentSold.setOnSSBDataLenChangedListener(this);
                return fragmentSold;
            case SSBBaseFragment.BOUGHT_POS:
                BoughtFragment fragmentBought = BoughtFragment.newInstance();
                fragmentBought.setOnSSBDataLenChangedListener(this);
                return fragmentBought;
        }
        return SellingFragment.newInstance();
    }

    @Override
    public int getCount() {
        // 若查看自己的则返回3，否则仅返回在售的1
        // if mine
        return 3;
        // else
        // return 1
    }

    @Override
    public int getItemPosition(@Nullable Object object) {
        return POSITION_NONE;
    }

    @Override
    public void onSSBDataLenChanged(int total, int page) {
        if (mContext instanceof SSBActivity) {
            if (page == ((SSBActivity) mContext).getCurrentItem()) {
                if (mOnSSBDataLenChangedIntermediaryListener != null)
                    mOnSSBDataLenChangedIntermediaryListener.onSSBDataLenChangedIntermediary(total);
            }
        }
    }

    /**
     * fragment与外面activity交流的中介接口
     */
    public interface OnSSBDataLenChangedIntermediaryListener {
        void onSSBDataLenChangedIntermediary(int total);
    }

    public void setOnSSBDataLenChangedIntermediaryListener(OnSSBDataLenChangedIntermediaryListener listener) {
        mOnSSBDataLenChangedIntermediaryListener = listener;
    }
}
