package com.example.schoolairdroprefactoredition.scene.main.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeBinding;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseParentFragment;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNavigatorAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.HomePagerAdapter;
import com.google.android.material.appbar.AppBarLayout;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

public class ParentPlaygroundFragment extends BaseParentFragment
        implements View.OnClickListener {

    public static ParentPlaygroundFragment newInstance() {
        return new ParentPlaygroundFragment();
    }

    private ViewPager mViewPager;

    private HomePagerAdapter homePagerAdapter;

    private AppBarLayout mAppbarLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).autoLogin();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);

        mViewPager = binding.homeViewpager;
        mAppbarLayout = binding.toolbarWrapper;

        homePagerAdapter = new HomePagerAdapter(getChildFragmentManager(), HomePagerAdapter.PAGE_TYPE_PLAYGROUND);
        setUpPlaceHolderHAndContainerView(binding.placeholder, binding.homeViewpager);

        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        HomeNavigatorAdapter adapter = new HomeNavigatorAdapter(getContext(), binding.homeViewpager, HomeNavigatorAdapter.HOME);
        commonNavigator.setAdapter(adapter);
        binding.homeViewpager.setAdapter(homePagerAdapter);
        binding.homeIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(binding.homeIndicator, binding.homeViewpager);

        binding.homeSearchBar.setOnClickListener(this);
        binding.homeTopAdd.setOnClickListener(this);
        binding.homeTopAdd.setText(R.string.addNewPost);
        binding.homeTopAdd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pen, 0, 0, 0);

        return binding.getRoot();
    }

    /**
     * 使列表滑动至顶部
     * 当当前页面最后可见的item位置小于一定值时直接调用平滑滑动
     * 否则将先闪现至固定item位置处再平滑滚动
     * <p>
     * 详见 {@link HomePlaygroundFragment#scrollToTop()}
     */
    public void pageScrollToTop() {
        if (mViewPager != null && mAppbarLayout != null) {
            homePagerAdapter.scrollToTop(mViewPager.getCurrentItem());
            mAppbarLayout.setExpanded(true, true);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.home_search_bar) {
            if (mOnSearchBarClickedListener != null) {
                mOnSearchBarClickedListener.onSearchBarClicked();
            }
        } else if (id == R.id.home_top_add) {
            onHomePostMyPosts(v);
        }
    }
}
