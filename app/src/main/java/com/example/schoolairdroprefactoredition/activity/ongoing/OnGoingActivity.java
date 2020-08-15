package com.example.schoolairdroprefactoredition.activity.ongoing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.adapter.OnGoingPagerAdapter;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.google.android.material.tabs.TabLayout;

public class OnGoingActivity extends AppCompatActivity {

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, OnGoingActivity.class);
        intent.putExtra(ConstantUtil.KEY_AUTHORIZE, bundle);
        context.startActivity(intent);
    }

    private ViewPager mPager;
    private TabLayout mTab;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        setContentView(R.layout.activity_tab);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar(findViewById(R.id.toolbar));
        ((TextView) findViewById(R.id.name)).setText(R.string.onGoing);

        mPager = findViewById(R.id.pager);
        mTab = findViewById(R.id.tab);

        mPager.setAdapter(new OnGoingPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this));
        mTab.setTabIndicatorFullWidth(false);
        mTab.setupWithViewPager(mPager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}