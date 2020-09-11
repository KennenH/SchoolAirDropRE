package com.example.schoolairdroprefactoredition.ui.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.schoolairdroprefactoredition.scene.ssb.fragment.BoughtFragment;
import com.example.schoolairdroprefactoredition.scene.ssb.fragment.SellingFragment;
import com.example.schoolairdroprefactoredition.scene.ssb.fragment.SoldFragment;

public class SSBPagerAdapter extends FragmentPagerAdapter {

    private Bundle bundle;

    public SSBPagerAdapter(@NonNull FragmentManager fm, int behavior, Bundle bundle) {
        super(fm, behavior);
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return SellingFragment.newInstance(bundle);
        else if (position == 1)
            return SoldFragment.newInstance(bundle);
        else
            return BoughtFragment.newInstance(bundle);
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
