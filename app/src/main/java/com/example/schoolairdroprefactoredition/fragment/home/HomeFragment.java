package com.example.schoolairdroprefactoredition.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.MainActivity;
import com.example.schoolairdroprefactoredition.activity.map.AMapActivity;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeBinding;
import com.example.schoolairdroprefactoredition.fragment.BaseMainFragment;
import com.example.schoolairdroprefactoredition.fragment.purchasing.PurchasingFragment;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNavigatorAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.HomePagerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.ErrorPlaceHolder;
import com.example.schoolairdroprefactoredition.ui.components.Location;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseMainFragment
        implements View.OnClickListener, MainActivity.OnLocationListener {

    private AMapLocation mLocation;

    private OnSearchBarClickedListener mOnSearchBarClickedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null && getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).setOnLocationListener(HomeFragment.this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);

        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, HomePagerAdapter.HOME);

        MagicIndicator indicator = binding.homeIndicator;
        ViewPager viewPager = binding.homeViewpager;
        Location mLocation = binding.homeLocation;

        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        HomeNavigatorAdapter adapter = new HomeNavigatorAdapter(getContext(), viewPager, HomeNavigatorAdapter.HOME);
        commonNavigator.setAdapter(adapter);
        viewPager.setAdapter(homePagerAdapter);
        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, viewPager);

        binding.homeSearchBar.setOnClickListener(this);
        mLocation.setOnClickListener(this);

        return binding.getRoot();
    }

    /**
     * 来自{@link AMapActivity}的定位回调结果
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == AMapActivity.REQUEST_CODE) {
                if (data != null) {
                    mLocation = data.getParcelableExtra(AMapActivity.LOCATION_KEY);
                    Log.d("HomeFragment", "callback from AMap activity" + mLocation.getAddress());
                    if (mOnLocationCallbackListener != null)
                        mOnLocationCallbackListener.onLocated(mLocation);
                    else
                        Log.d("HomeFragment", "mOnLocationCallbackListener is null");
                }
            } else {
                Log.d("HomeFragment", "requestCode not compatible");
            }
        } else {
            Log.d("HomeFragment", "resultCode not ok");
        }
    }

    @Override
    public void onLocated(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                if (mOnLocationCallbackListener != null)
                    mOnLocationCallbackListener.onLocated(aMapLocation);
                Log.d("HomeFragment", "callback from Main Activity" + aMapLocation.getAddress());
            } else {
                Log.d("HomeFragment", aMapLocation.getErrorInfo());
            }
        } else {
            Log.d("HomeFragment", "callback from main is null");
        }
    }

    public interface OnSearchBarClickedListener {
        /**
         * 点击了搜索框,打开搜索页面
         */
        void onSearchBarClicked();
    }

    public void setOnSearchBarClickListener(OnSearchBarClickedListener listener) {
        this.mOnSearchBarClickedListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.home_search_bar) {
            if (mOnSearchBarClickedListener != null)
                mOnSearchBarClickedListener.onSearchBarClicked();
        } else if (id == R.id.home_location) {
            AMapActivity.start(getContext());
        }
    }
}
