package com.example.schoolairdroprefactoredition.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.map.AMapActivity;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeBinding;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNavigatorAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.HomePagerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.Location;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

public class HomeFragment extends Fragment
        implements View.OnClickListener, AMapLocationListener {

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private Location mLocation;

    private OnSearchBarClickedListener mOnSearchBarClickedListener;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);

        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, HomePagerAdapter.HOME);
        mLocationClient = new AMapLocationClient(getContext());
        mLocationOption = new AMapLocationClientOption();

        MagicIndicator indicator = binding.homeIndicator;
        ViewPager viewPager = binding.homeViewpager;
        mLocation = binding.homeLocation;

        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        HomeNavigatorAdapter adapter = new HomeNavigatorAdapter(getContext(), viewPager, HomeNavigatorAdapter.HOME);
        commonNavigator.setAdapter(adapter);
        viewPager.setAdapter(homePagerAdapter);
        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, viewPager);

        binding.homeSearchBar.setOnClickListener(this);
        mLocation.setOnClickListener(this);

        initLocation();

        return binding.getRoot();
    }

    private void initLocation() {
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setLocationCacheEnable(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(this);
        mLocationClient.startLocation();
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
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mLocation.setLocation(aMapLocation.getCity());
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.home_search_bar) {
            if (mOnSearchBarClickedListener != null) {
                mOnSearchBarClickedListener.onSearchBarClicked();
            }
        } else if (id == R.id.home_location) {
            if (getContext() != null)
                AMapActivity.start(getContext());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }

}
