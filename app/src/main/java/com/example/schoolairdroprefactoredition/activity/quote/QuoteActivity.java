package com.example.schoolairdroprefactoredition.activity.quote;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.adapter.QuoteSectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class QuoteActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_quote);

        mTabLayout = findViewById(R.id.quote_tab);
        mPager = findViewById(R.id.quote_pager);

        mPager.setAdapter(new QuoteSectionsPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        mTabLayout.setupWithViewPager(mPager);
    }
}