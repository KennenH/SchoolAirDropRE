package com.example.schoolairdroprefactoredition.ui.adapter;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.schoolairdroprefactoredition.R;

public class QuoteSectionsPagerAdapter extends FragmentPagerAdapter {

    @IdRes
    private static final int[] TABS = new int[]{R.string.received,R.string.sent};

    public QuoteSectionsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return TABS.length;
    }
}
