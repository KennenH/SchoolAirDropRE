package com.example.schoolairdroprefactoredition.scene.tab.ongoing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.tab.TabBaseActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.OnGoingPagerAdapter;

import org.jetbrains.annotations.NotNull;

public class OnGoingActivity extends TabBaseActivity {

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, OnGoingActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @NotNull
    @Override
    public PagerAdapter getPagerAdapter() {
        return new OnGoingPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);
    }

    @Override
    public int getPageNameRes() {
        return R.string.onGoing;
    }
}