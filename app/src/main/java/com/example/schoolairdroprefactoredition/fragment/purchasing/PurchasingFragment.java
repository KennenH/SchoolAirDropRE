package com.example.schoolairdroprefactoredition.fragment.purchasing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.amap.api.location.AMapLocation;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.MainActivity;
import com.example.schoolairdroprefactoredition.activity.map.AMapActivity;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeBinding;
import com.example.schoolairdroprefactoredition.fragment.BaseMainFragment;
import com.example.schoolairdroprefactoredition.fragment.home.HomeFragment;
import com.example.schoolairdroprefactoredition.fragment.home.HomeNearbyFragment;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNavigatorAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.HomePagerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.ErrorPlaceHolder;
import com.example.schoolairdroprefactoredition.ui.components.Location;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import static android.app.Activity.RESULT_OK;

public class PurchasingFragment extends BaseMainFragment implements View.OnClickListener, MainActivity.OnLocationListener, HomeNearbyFragment.OnLocationCalledBackListener {
    private HomeFragment.OnSearchBarClickedListener mOnSearchBarClickedListener;

    private AMapLocation mLocation;

    private ErrorPlaceHolder mPlaceholder;
    private CoordinatorLayout mGoodsContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null && getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).setOnLocationListener(PurchasingFragment.this);
        Fragment fragment = getChildFragmentManager().getFragments().get(0);
        if (fragment instanceof HomeNearbyFragment)
            ((HomeNearbyFragment) fragment).setOnLocationCalledBackListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, HomePagerAdapter.PURCHASING);

        MagicIndicator indicator = binding.homeIndicator;
        ViewPager viewPager = binding.homeViewpager;
        Location mLocation = binding.homeLocation;
        mPlaceholder = binding.placeholder;
        mGoodsContainer = binding.goodsContainer;
        showGoodsContainer();

        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        HomeNavigatorAdapter adapter = new HomeNavigatorAdapter(getContext(), viewPager, HomeNavigatorAdapter.PURCHASING);
        commonNavigator.setAdapter(adapter);
        viewPager.setAdapter(homePagerAdapter);
        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, viewPager);

        binding.homeSearchBar.setOnClickListener(this);
        mLocation.setOnClickListener(this);

        return binding.getRoot();
    }

    /**
     * 显示placeholder 隐藏recycler list
     */
    private void showPlaceholder() {
        mPlaceholder.setVisibility(View.VISIBLE);
        mGoodsContainer.setVisibility(View.GONE);
    }

    /**
     * 显示recycler list 隐藏placeholder
     */
    private void showGoodsContainer() {
        mPlaceholder.setVisibility(View.GONE);
        mGoodsContainer.setVisibility(View.VISIBLE);
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
                    Log.d("PurchasingFragment", "callback from AMap activity" + mLocation.getAddress());
                    if (mOnLocationCallbackListener != null)
                        mOnLocationCallbackListener.onLocated(mLocation);
                }
            }
        }
    }

    /**
     * 来自子fragment{@link HomeNearbyFragment}的回调通知
     * 在此两个结果回调中更新placeholder
     */
    @Override
    public void onLocationSuccess() {
        showGoodsContainer();
    }

    /**
     * 来自子fragment{@link HomeNearbyFragment}的回调通知
     * 在此两个结果回调中更新placeholder
     */
    @Override
    public void onLocationFailed() {
        showPlaceholder();
    }

    /**
     * 来自{@link MainActivity}的定位回调结果
     */
    @Override
    public void onLocated(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                if (mOnLocationCallbackListener != null)
                    mOnLocationCallbackListener.onLocated(aMapLocation);
                Log.d("PurchasingFragment", "callback from main activity" + aMapLocation.getAddress());
            } else {
                Log.d("PurchasingFragment", aMapLocation.getErrorInfo());
            }
        } else {
            Log.d("PurchasingFragment", "callbacl null");
        }
    }

    /**
     * 设置搜索框点击回调事件
     */
    public void setOnSearchBarClickListener(HomeFragment.OnSearchBarClickedListener listener) {
        this.mOnSearchBarClickedListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.home_search_bar) {
            if (mOnSearchBarClickedListener != null) {
                mOnSearchBarClickedListener.onSearchBarClicked();
            }
        } else if (id == R.id.home_location) {
            AMapActivity.start(getContext());
        }
    }
}
