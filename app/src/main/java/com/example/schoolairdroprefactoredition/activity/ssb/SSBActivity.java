package com.example.schoolairdroprefactoredition.activity.ssb;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.SSBPagerAdapter;

public class SSBActivity extends ImmersionStatusBarActivity implements View.OnClickListener {

    public static final int SELLING = R.string.selling;
    public static final int SOLD = R.string.sold;
    public static final int BOUGHT = R.string.purchased;

    public static final String PAGE_INDEX = "SSBPageIndex";

    public static void start(Context context, int index) {
        Intent intent = new Intent(context, SSBActivity.class);
        intent.putExtra(PAGE_INDEX, index);
        context.startActivity(intent);
    }

    private EditText mSearch;
    private TextView mTitle;

    private ViewPager mPager;
    private SSBPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssb);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar(findViewById(R.id.toolbar));

        findViewById(R.id.ssb_search).setOnClickListener(this);
        findViewById(R.id.ssb_title).setOnClickListener(this);

        mPager = findViewById(R.id.ssb_pager);
        mSearch = findViewById(R.id.ssb_search);
        mTitle = findViewById(R.id.ssb_title);

        Intent intent = getIntent();
        int index = intent.getIntExtra(PAGE_INDEX, 0);
        mPagerAdapter = new SSBPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                KeyboardUtils.showSoftInput(v);
            else
                KeyboardUtils.hideSoftInput(v);
        });
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                invalidateOptionsMenu();
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    mTitle.setText(SELLING);
                else if (position == 1)
                    mTitle.setText(SOLD);
                else
                    mTitle.setText(BOUGHT);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mPager.setOffscreenPageLimit(0);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(index);
    }

    public void hideSearchBar() {
        mTitle.requestFocus();
        mSearch.clearFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int now = mPager.getCurrentItem();
        final MenuItem add = menu.findItem(R.id.ssb_selling_add);
        if (add != null) {
            if (now == 0)
                add.setVisible(true);
            else
                add.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ssb_search) {
            // open search page
        }
    }
}