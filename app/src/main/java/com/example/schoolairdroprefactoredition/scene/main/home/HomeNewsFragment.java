package com.example.schoolairdroprefactoredition.scene.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.amap.api.location.AMapLocation;
import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragment;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.scene.posts.PostsActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNewsRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

public class HomeNewsFragment extends BaseChildFragment implements
        OnRefreshListener, EndlessRecyclerView.OnLoadMoreListener,
        BaseStateViewModel.OnRequestListener, MainActivity.OnLoginStateChangedListener,
        MainActivity.OnLocationListener, HomeNewsRecyclerAdapter.OnNoMoreDataListener, HomeNewsRecyclerAdapter.OnHomePostActionClickListener {

    private FragmentHomeContentBinding binding;

    private HomeNewsFragmentViewModel homeContentFragmentViewModel;

    private HomeNewsRecyclerAdapter mHomeNewsRecyclerAdapter;

    private AMapLocation mLocation;

    public static HomeNewsFragment newInstance() {
        return new HomeNewsFragment();
    }

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
        binding = FragmentHomeContentBinding.inflate(inflater, container, false);
        homeContentFragmentViewModel = new ViewModelProvider(this).get(HomeNewsFragmentViewModel.class);
        homeContentFragmentViewModel.setOnRequestListener(this);

        initRecycler();

        return binding.getRoot();
    }

    private void initRecycler() {
        binding.homeRefresh.setOnRefreshListener(this);
        binding.homeRecycler.setOnLoadMoreListener(this);

        StaggeredGridLayoutManager mManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        binding.homeRecycler.setPadding(SizeUtils.dp2px(5f), SizeUtils.dp2px(5f), SizeUtils.dp2px(5f), SizeUtils.dp2px(5f));
        binding.homeRecycler.setLayoutManager(mManager);
        binding.homeRecycler.addItemDecoration(new MarginItemDecoration(SizeUtils.dp2px(1f), true));

        mHomeNewsRecyclerAdapter = new HomeNewsRecyclerAdapter();
        mHomeNewsRecyclerAdapter.setOnHomePostItemClickListener(this);
        mHomeNewsRecyclerAdapter.setOnNoMoreDataListener(this);
        binding.homeRecycler.setAdapter(mHomeNewsRecyclerAdapter);
    }

    /**
     * 来自{@link MainActivity}的定位回调
     */
    @Override
    public void onLocated(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mLocation = aMapLocation;
                homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
                    if (data.size() < 1) {
                        showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_NEWS_HOME);
                    } else {
                        mHomeNewsRecyclerAdapter.setList(data);
                        showContentContainer();
                    }
                });
            } else {
                showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME);
            }
        } else {
            showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME);
        }
    }

    @Override
    public void onPermissionDenied() {
        showPlaceHolder(StatePlaceHolder.TYPE_DENIED);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mLocation != null) {
            homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
                refreshLayout.finishRefresh();
                if (data.size() < 1) {
                    showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_NEWS_HOME);
                } else {
                    mHomeNewsRecyclerAdapter.setList(data);
                    showContentContainer();
                }
            });
        } else { // 定位失败时通知父Fragment显示PlaceHolder
            refreshLayout.finishRefresh();
            showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME);
            locateWithoutRequest();// 自动请求MainActivity的定位
        }
    }

    @Override
    public void autoLoadMore(EndlessRecyclerView recycler) {
//        homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
//            mHomeNewsRecyclerAdapter.addData(data);
//            showContentContainer();
//            recycler.finishLoading();
//        });
    }

    @Override
    public void onError() {
        showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void onLoginStateChanged() {

    }

    @Override
    public void onNoMoreData() {
        binding.homeRecycler.setIsNoMoreData(true);
    }

    @Override
    public void onNoMoreDataRefresh() {
        binding.homeRecycler.setIsNoMoreData(false);
    }

    @Override
    public void onHomePostItemClicked(@NotNull CardView pager, @NotNull TextView title) {
        try {
            Intent intent = new Intent(getContext(), PostsActivity.class);
            Pair<View, String> pair1 = Pair.create(pager, getString(R.string.sharedElementPostActivityWrapper));
            Pair<View, String> pair2 = Pair.create(title, getString(R.string.sharedElementPostActivityTitle));
//            Pair<View, String> pair3 = Pair.create(avatar, getString(R.string.sharedElementPostActivityAvatar));
//            Pair<View, String> pair4 = Pair.create(name, getString(R.string.sharedElementPostActivityName));
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pair1, pair2);
            startActivity(intent, options.toBundle());
        } catch (NullPointerException ignored) {
        }
    }
}
