package com.example.schoolairdroprefactoredition.activity.quote;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.QuotePagerAdapter;
import com.google.android.material.tabs.TabLayout;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class QuoteActivity extends ImmersionStatusBarActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, QuoteActivity.class);
        context.startActivity(intent);
    }

    private TabLayout mTabLayout;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_tab);
        setSupportActionBar(findViewById(R.id.toolbar));
        ((TextView) findViewById(R.id.name)).setText(R.string.quote);


        mTabLayout = findViewById(R.id.tab);
        mPager = findViewById(R.id.pager);

        mPager.setAdapter(new QuotePagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this));
        mTabLayout.setTabIndicatorFullWidth(false);
        mTabLayout.setupWithViewPager(mPager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}