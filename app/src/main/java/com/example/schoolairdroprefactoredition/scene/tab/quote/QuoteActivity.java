package com.example.schoolairdroprefactoredition.scene.tab.quote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.PagerAdapter;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.tab.TabBaseActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.QuotePagerAdapter;

import org.jetbrains.annotations.NotNull;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class QuoteActivity extends TabBaseActivity {

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, QuoteActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @NotNull
    @Override
    public PagerAdapter getPagerAdapter() {
        return new QuotePagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);
    }

    @Override
    public int getPageNameRes() {
        return R.string.quote;
    }
}