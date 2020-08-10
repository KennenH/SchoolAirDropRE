package com.example.schoolairdroprefactoredition.ui.adapter;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.fragment.quote.QuoteFragment;

public class QuotePagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    @IdRes
    private static final int[] TABS = new int[]{R.string.received, R.string.sent};

    public QuotePagerAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        mContext = context;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TABS[position]);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return QuoteFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return TABS.length;
    }
}
