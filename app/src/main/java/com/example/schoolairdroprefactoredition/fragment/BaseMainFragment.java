package com.example.schoolairdroprefactoredition.fragment;

import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocation;
import com.example.schoolairdroprefactoredition.activity.MainActivity;
import com.example.schoolairdroprefactoredition.activity.map.AMapActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.HomePagerAdapter;

public class BaseMainFragment extends Fragment {
    protected OnLocationCallbackListener mOnLocationCallbackListener;

    /**
     * 定位回调时的回调
     * 多源单一去向，来自{@link MainActivity}{@link AMapActivity}
     * 去向{@link HomePagerAdapter}
     */
    public interface OnLocationCallbackListener {
        void onLocated(AMapLocation aMapLocation);
    }

    public void setOnLocationCallbackListener(OnLocationCallbackListener listener) {
        mOnLocationCallbackListener = listener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOnLocationCallbackListener = null;
    }
}
