package com.example.schoolairdroprefactoredition.scene.main.home;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.amap.api.location.AMapLocation;
import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragment;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.scene.posts.PostsActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNewsRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import org.jetbrains.annotations.NotNull;

public class HomePlaygroundFragment extends BaseChildFragment implements
        BaseStateViewModel.OnRequestListener,
        HomeNewsRecyclerAdapter.OnNoMoreDataListener, HomeNewsRecyclerAdapter.OnHomePostActionClickListener {

    private HomePlaygroundFragmentViewModel homeContentFragmentViewModel;

    private HomeNewsRecyclerAdapter mHomeNewsRecyclerAdapter;

    private EndlessRecyclerView mHomeRecycler;

    private StaggeredGridLayoutManager mManager;

    public static HomePlaygroundFragment newInstance() {
        return new HomePlaygroundFragment();
    }

    @Override
    public void initView(FragmentHomeContentBinding binding) {
        homeContentFragmentViewModel = new ViewModelProvider(this).get(HomePlaygroundFragmentViewModel.class);
        homeContentFragmentViewModel.setOnRequestListener(this);

        mHomeRecycler = binding.homeRecycler;

        binding.homeRecycler.setOnLoadMoreListener(this);

        mManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
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
     * 使列表滑动至顶部
     * 当当前页面最后可见的item位置小于一定值时直接调用平滑滑动
     * 否则将先闪现至固定item位置处再平滑滚动
     */
    public void scrollToTop() {
        int[] visible = new int[5];
        mManager.findLastVisibleItemPositions(visible);
        if (visible[0] > 10) {
            mHomeRecycler.scrollToPosition(5);
        }
        mHomeRecycler.smoothScrollToPosition(0);
    }

    @Override
    public void getOnlineData(AMapLocation aMapLocation) {
        homeContentFragmentViewModel.getHomeNews().observe(getViewLifecycleOwner(), data -> {
            if (data.size() < 1) {
                showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_HOME_POST);
            } else {
                mHomeNewsRecyclerAdapter.setList(data);
                showContentContainer();
            }
        });
    }

    @Override
    public void getRefreshData(@NonNull RefreshLayout refreshLayout, AMapLocation aMapLocation) {
        homeContentFragmentViewModel.getHomeNews(aMapLocation.getLongitude(), aMapLocation.getLatitude()).observe(getViewLifecycleOwner(), data -> {
            refreshLayout.finishRefresh();
            if (data.size() < 1) {
                showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_HOME_POST);
            } else {
                mHomeNewsRecyclerAdapter.setList(data);
                showContentContainer();
            }
        });
    }

    @Override
    public void getAutoLoadMoreData(AMapLocation aMapLocation) {
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
    public void onNoMoreData() {
        mHomeRecycler.setIsNoMoreData(true);
    }

    @Override
    public void onNoMoreDataRefresh() {
        mHomeRecycler.setIsNoMoreData(false);
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
