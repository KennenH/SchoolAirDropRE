package com.example.schoolairdroprefactoredition.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.schoolairdroprefactoredition.fragment.home.HomeNearbyFragment;
import com.example.schoolairdroprefactoredition.fragment.home.HomeNewsFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    public HomePagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeNewsFragment();
        }
        return new HomeNearbyFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(@Nullable Object object) {
        return POSITION_NONE;
    }
}