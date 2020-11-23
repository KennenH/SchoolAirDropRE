package com.example.schoolairdroprefactoredition.scene.main.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocation;
import com.blankj.utilcode.constant.PermissionConstants;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding;
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

public abstract class BaseChildFragment extends Fragment implements MainActivity.OnLocationListener, EndlessRecyclerView.OnLoadMoreListener, OnRefreshListener {

    /**
     * 只在第一个子页面加载时才有主动请求定位的权限
     * 但是这里会有一个问题，切换app主题时由于requested已置为true
     * 因此页面被recreated的时候就不会再次自动获取数据
     * 解决：需要在{@link MainActivity}的OnDestroy中将requested重置
     */
    private static boolean requested = false;

    /**
     * 是否已经获取来自{@link MainActivity}的定位信息
     * <p>
     * 1、先入为主的页面将有权力主动要求MainActivity获取定位信息
     * 2、后来者若发现页面中已经存在定位信息，则可以直接使用它获取数据
     * 3、后来者若发现页面中不存在定位信息，则将等待主页面返回定位信息
     * 4、在主页的定位信息返回之后，若发现该页面已经被通知过了，则忽略
     * 该页面的数据获取，否则获取页面数据
     */
    private boolean isThisPageLocationGot = false; // 本页面是否收到过定位信息
    private static boolean isMainActivityLocated = false; // 其他页面是否受到过定位信息

    private static AMapLocation aMapLocation = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).addOnLocationListener(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentHomeContentBinding binding = FragmentHomeContentBinding.inflate(inflater, container, false);

        binding.homeRecycler.setOnLoadMoreListener(this);
        binding.homeRefresh.setOnRefreshListener(this);

        initView(binding);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showPlaceHolder(StatePlaceHolder.TYPE_LOADING);

        if (!requested) {
            requested = true;
            locate(PermissionBaseActivity.RequestType.AUTO);// 自动请求MainActivity的定位
        } else if (isMainActivityLocated) {
            getPageDataIfLocated();
        }
    }

    public abstract void initView(FragmentHomeContentBinding binding);

    /**
     * 定位相关的页面数据获取
     */
    private void getPageDataIfLocated() {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                getOnlineData(aMapLocation);
            } else {
                showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME);
            }
        } else {
            showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME);
        }
    }

    /**
     * 获取页面数据
     */
    public abstract void getOnlineData(AMapLocation aMapLocation);

    /**
     * 下拉刷新获取数据
     */
    public abstract void getRefreshData(@NonNull RefreshLayout refreshLayout, AMapLocation aMapLocation);

    /**
     * 下拉自动获取更多数据
     */
    public abstract void getAutoLoadMoreData(AMapLocation aMapLocation);

    /**
     * 通知主题改变
     * 重置所有静态变量
     */
    public static void notifyThemeChanged() {
        requested = false;
        isMainActivityLocated = false;
        aMapLocation = null;
    }

    /**
     * 显示PlaceHolder
     *
     * @param type {@link com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder#TYPE_NETWORK_OR_LOCATION_ERROR_HOME}
     *             {@link com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder#TYPE_EMPTY_GOODS_HOME}
     */
    protected void showPlaceHolder(int type) {
        if (getParentFragment() instanceof BaseParentFragment) {
            ((BaseParentFragment) getParentFragment()).showPlaceholder(type);
        }
    }

    /**
     * 显示物品信息
     */
    protected void showContentContainer() {
        if (getParentFragment() instanceof BaseParentFragment) {
            ((BaseParentFragment) getParentFragment()).showContentContainer();
        }
    }

    /**
     * 请求{@link MainActivity#requestPermission(String, int)} (int)} ()}的定位
     */
    protected void locate(@PermissionBaseActivity.RequestType int type) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).requestPermission(PermissionConstants.LOCATION, type);
        }
    }

    /**
     * 请求定位但是不请求权限
     */
    protected void locateWithoutRequest() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).checkPermissionWithoutRequest(PermissionBaseActivity.Automatically.LOCATION);
        }
    }

    @Override
    public void onLocated(AMapLocation aMapLocation) {
        BaseChildFragment.aMapLocation = aMapLocation;
        isMainActivityLocated = true;
        if (!isThisPageLocationGot) {
            isThisPageLocationGot = true;
            getPageDataIfLocated();
        }
    }

    @Override
    public void onPermissionDenied() {
        showPlaceHolder(StatePlaceHolder.TYPE_DENIED);
    }

    @Override
    public void autoLoadMore(EndlessRecyclerView recycler) {
        if (aMapLocation != null) {
            getAutoLoadMoreData(aMapLocation);
        } else {
            showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
            recycler.finishLoading();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (aMapLocation != null) {
            getRefreshData(refreshLayout, aMapLocation);
        } else { // 定位失败时通知父Fragment显示PlaceHolder
            refreshLayout.finishRefresh();
            showPlaceHolder(StatePlaceHolder.TYPE_LOADING);
            locateWithoutRequest();// 自动请求MainActivity的定位
        }
    }
}
