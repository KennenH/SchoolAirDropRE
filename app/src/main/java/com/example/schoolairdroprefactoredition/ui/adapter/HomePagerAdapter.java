package com.example.schoolairdroprefactoredition.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.schoolairdroprefactoredition.scene.main.home.HomePlaygroundFragment;
import com.example.schoolairdroprefactoredition.scene.main.home.HomePurchasingFragment;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {
    public static final int PAGE_TYPE_PLAYGROUND = 213; // 页面类型码 广场
    public static final int PAGE_TYPE_PURCHASING = 321; // 页面类型码 淘物

    public static final int PAGE_NUM_PLAYGROUND = 1; // 子页面数量 广场
    public static final int PAGE_NUM_PURCHASING = 1; // 子页面数量 淘物

    private final List<HomePlaygroundFragment> mPlaygroundFragments = new ArrayList<>(PAGE_NUM_PLAYGROUND);
    private final List<HomePurchasingFragment> mPurchasingFragments = new ArrayList<>(PAGE_NUM_PURCHASING);

    private final int pageType;

    public HomePagerAdapter(@NonNull FragmentManager fm, int page) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pageType = page;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (pageType == PAGE_TYPE_PLAYGROUND) {
            HomePlaygroundFragment fragment = HomePlaygroundFragment.newInstance();
            mPlaygroundFragments.add(fragment);
            return fragment;
        } else {
            HomePurchasingFragment fragment = HomePurchasingFragment.newInstance();
            mPurchasingFragments.add(fragment);
            return fragment;
        }
    }

    /**
     * 使列表滑动至顶部
     * 当当前页面最后可见的item位置小于一定值时直接调用平滑滑动
     * 否则将先闪现至固定item位置处再平滑滚动
     */
    public void scrollToTop(int index) {
        if (pageType == PAGE_TYPE_PLAYGROUND) {
            mPlaygroundFragments.get(index).scrollToTop();
        } else {
            mPurchasingFragments.get(index).scrollToTop();
        }
    }

    @Override
    public int getCount() {
        if (pageType == PAGE_TYPE_PLAYGROUND) {
            return PAGE_NUM_PLAYGROUND;
        } else if (pageType == PAGE_TYPE_PURCHASING) {
            return PAGE_NUM_PURCHASING;
        } else {
            return 1;
        }
    }
}