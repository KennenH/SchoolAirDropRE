package com.example.schoolairdroprefactoredition.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.scene.ssb.fragment.SSBBaseFragment;
import com.example.schoolairdroprefactoredition.scene.ssb.fragment.SellingFragment;

public class SSBPagerAdapter extends FragmentPagerAdapter implements SSBBaseFragment.OnSSBDataLenChangedListener {

    private final Context mContext;
    private OnSSBDataLenChangedIntermediaryListener mOnSSBDataLenChangedIntermediaryListener;

    private final boolean isMine;

    public SSBPagerAdapter(Context context, @NonNull FragmentManager fm, int behavior, boolean isMine) {
        super(fm, behavior);
        mContext = context;
        this.isMine = isMine;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        SellingFragment fragmentSelling = SellingFragment.newInstance(isMine);
        fragmentSelling.setOnSSBDataLenChangedListener(this);
        return fragmentSelling;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public int getItemPosition(@Nullable Object object) {
        return POSITION_NONE;
    }

    @Override
    public void onSSBDataLenChanged(int total, int page) {
        if (mContext instanceof SSBActivity) {
            if (page == ((SSBActivity) mContext).getCurrentItem()) {
                if (mOnSSBDataLenChangedIntermediaryListener != null) {
                    mOnSSBDataLenChangedIntermediaryListener.onSSBDataLenChangedIntermediary(total);
                }
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
