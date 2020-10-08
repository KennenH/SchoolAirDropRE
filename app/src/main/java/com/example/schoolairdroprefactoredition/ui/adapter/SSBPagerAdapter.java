package com.example.schoolairdroprefactoredition.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.schoolairdroprefactoredition.scene.ssb.fragment.BoughtFragment;
import com.example.schoolairdroprefactoredition.scene.ssb.fragment.SellingFragment;
import com.example.schoolairdroprefactoredition.scene.ssb.fragment.SoldFragment;

public class SSBPagerAdapter extends FragmentPagerAdapter {

    public SSBPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return SellingFragment.newInstance();
        else if (position == 1)
            return SoldFragment.newInstance();
        else
            return BoughtFragment.newInstance();
    }

    @Override
    public int getCount() {
        // 若查看自己的则返回3，否则仅返回在售的1
        // if mine
        return 3;
        // else
        // return 1
    }

    @Override
    public int getItemPosition(@Nullable Object object) {
        return POSITION_NONE;
    }
}
