package com.example.schoolairdroprefactoredition.ui.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.schoolairdroprefactoredition.scene.main.home.HomeNearbyFragment;
import com.example.schoolairdroprefactoredition.scene.main.home.HomeNewsFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    public static final int HOME = 3;
    public static final int PURCHASING = 1;

    private int mIndex;

    private Bundle bundle;

    public HomePagerAdapter(@NonNull FragmentManager fm, int behavior, int page) {
        super(fm, behavior);
        mIndex = page;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (mIndex == HOME)
            return new HomeNewsFragment();
        else
            return new HomeNearbyFragment();
    }

    @Override
    public int getCount() {
        return mIndex;
    }

    @Override
    public int getItemPosition(@Nullable Object object) {
        return POSITION_NONE;
    }
}