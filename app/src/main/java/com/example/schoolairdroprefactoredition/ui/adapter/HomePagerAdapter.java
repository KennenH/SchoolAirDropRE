package com.example.schoolairdroprefactoredition.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.schoolairdroprefactoredition.scene.main.home.HomePurchasingFragment;
import com.example.schoolairdroprefactoredition.scene.main.home.HomePlaygroundFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    public static final int PAGE_TYPE_PLAYGROUND = 213; // 页面类型码 广场
    public static final int PAGE_TYPE_PURCHASING = 321; // 页面类型码 淘物

    public static final int PAGE_NUM_PLAYGROUND = 1; // 子页面数量 广场
    public static final int PAGE_NUM_PURCHASING = 1; // 子页面数量 淘物

    private final int pageType;

    public HomePagerAdapter(@NonNull FragmentManager fm, int page) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pageType = page;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (pageType == PAGE_TYPE_PLAYGROUND) {
            return HomePlaygroundFragment.newInstance();
        } else {
            return HomePurchasingFragment.newInstance();
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

    @Override
    public int getItemPosition(@Nullable Object object) {
        return POSITION_NONE;
    }
}