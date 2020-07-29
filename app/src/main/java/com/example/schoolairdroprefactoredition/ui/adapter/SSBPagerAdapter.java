package com.example.schoolairdroprefactoredition.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.schoolairdroprefactoredition.fragment.ssb.BoughtFragment;
import com.example.schoolairdroprefactoredition.fragment.ssb.SellingFragment;
import com.example.schoolairdroprefactoredition.fragment.ssb.SoldFragment;

public class SSBPagerAdapter extends FragmentPagerAdapter {
    public SSBPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new SellingFragment();
        else if (position == 1)
            return new SoldFragment();
        else
            return new BoughtFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getItemPosition(@Nullable Object object) {
        return POSITION_NONE;
    }
}
