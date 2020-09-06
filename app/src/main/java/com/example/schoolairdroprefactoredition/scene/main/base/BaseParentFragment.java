package com.example.schoolairdroprefactoredition.scene.main.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.amap.api.location.AMapLocation;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.addnew.SellingAddNewActivity;
import com.example.schoolairdroprefactoredition.scene.map.AMapActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.HomePagerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;

import static android.app.Activity.RESULT_OK;

public class BaseParentFragment extends Fragment implements StatePlaceHolder.OnPlaceHolderActionListener, MainActivity.OnLocationListener {

    private StatePlaceHolder mPlaceHolder;
    private ViewPager mContentContainer;

    protected OnLocationCallbackListener mOnLocationCallbackListener;
    protected OnSearchBarClickedListener mOnSearchBarClickedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null && getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).setOnLocationListener(this);
    }

    protected void setUpPlaceHolderHAndGoodsContainer(StatePlaceHolder placeHolder, ViewPager goodsContainer) {
        mPlaceHolder = placeHolder;
        mContentContainer = goodsContainer;

        mPlaceHolder.setOnPlaceHolderActionListener(this);
    }

    /**
     * 来自
     * {@link AMapActivity}的定位回调结果
     * {@link AMapActivity#LOCATION_KEY}
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == AMapActivity.REQUEST_CODE) {
                if (data != null) {
                    if (mOnLocationCallbackListener != null)
                        mOnLocationCallbackListener.onLocated(data.getParcelableExtra(AMapActivity.LOCATION_KEY));
                }
            }
        }
    }

    /**
     * 来自{@link MainActivity}的定位回调
     * 将结果回调至所有子fragment
     *
     * @param aMapLocation
     */
    @Override
    public void onLocated(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                if (mOnLocationCallbackListener != null)
                    mOnLocationCallbackListener.onLocated(aMapLocation);
            } else
                mOnLocationCallbackListener.onLocationError();
        }
    }

    /**
     * 来自{@link MainActivity}的定位回调
     * 定位权限已被拒绝
     */
    @Override
    public void onPermissionDenied() {
        showPlaceholder(StatePlaceHolder.TYPE_DENIED);
    }

    /**
     * 显示placeholder 隐藏recycler list
     *
     * @param type {@link StatePlaceHolder#TYPE_ERROR}
     *             {@link StatePlaceHolder#TYPE_EMPTY}
     */
    public void showPlaceholder(int type) {
        if (mPlaceHolder == null || mContentContainer == null)
            throw new NullPointerException("mPlaceHolder or mGoodsContainer can not be null");

        mPlaceHolder.setPlaceHolderType(type);
        mPlaceHolder.setVisibility(View.VISIBLE);
        mContentContainer.setVisibility(View.GONE);
    }

    /**
     * 显示recycler list 隐藏placeholder
     */
    public void showContentContainer() {
        if (mPlaceHolder == null || mContentContainer == null)
            throw new NullPointerException("mPlaceHolder or mGoodsContainer can not be null");

        mPlaceHolder.setVisibility(View.GONE);
        mContentContainer.setVisibility(View.VISIBLE);
    }

    /**
     * PlaceHolder动作回调{@link StatePlaceHolder.OnPlaceHolderActionListener}
     * 重试
     */
    @Override
    public void onRetry(View view) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).requestLocationPermission(PermissionBaseActivity.RequestType.MANUAL);
        }
    }

    /**
     * PlaceHolder动作回调{@link StatePlaceHolder.OnPlaceHolderActionListener}
     * 发布我的物品
     */
    @Override
    public void onPostMyItem(View view) {
        SellingAddNewActivity.startForResult(getContext());
    }


    /**
     * 定位回调时的回调
     * 多源单一去向，来自{@link MainActivity}{@link AMapActivity}
     * 去向{@link HomePagerAdapter}
     */
    public interface OnLocationCallbackListener {
        void onLocated(AMapLocation aMapLocation);

        void onLocationError();
    }

    public void setOnLocationCallbackListener(OnLocationCallbackListener listener) {
        mOnLocationCallbackListener = listener;
    }

    /**
     * 搜索框点击时的回调
     */
    public interface OnSearchBarClickedListener {
        void onSearchBarClicked();
    }

    public void setOnSearchBarClickedListener(OnSearchBarClickedListener listener) {
        mOnSearchBarClickedListener = listener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOnLocationCallbackListener = null;
    }
}
