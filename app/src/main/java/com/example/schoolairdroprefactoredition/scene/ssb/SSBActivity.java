package com.example.schoolairdroprefactoredition.scene.ssb;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ActivitySsbBinding;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.SSBPagerAdapter;

public class SSBActivity extends ImmersionStatusBarActivity implements View.OnClickListener {

    public static final int SELLING = R.string.selling;
    public static final int SOLD = R.string.sold;
    public static final int BOUGHT = R.string.purchased;

    public static final String PAGE_INDEX = "SSBPageIndex";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SSBActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private ActivitySsbBinding binding;
    private SSBPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySsbBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar(findViewById(R.id.toolbar));

        binding.ssbSearch.setOnClickListener(this);
        binding.ssbTitle.setOnClickListener(this);

        Intent intent = getIntent();
        int index = intent.getIntExtra(PAGE_INDEX, 0);
        mPagerAdapter = new SSBPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, getIntent().getExtras());

        binding.ssbSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                KeyboardUtils.showSoftInput(v);
            else
                KeyboardUtils.hideSoftInput(v);
        });
        binding.ssbPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                invalidateOptionsMenu();
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    binding.ssbTitle.setText(SELLING);
                else if (position == 1)
                    binding.ssbTitle.setText(SOLD);
                else
                    binding.ssbTitle.setText(BOUGHT);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        binding.ssbPager.setOffscreenPageLimit(3);
        binding.ssbPager.setAdapter(mPagerAdapter);
        binding.ssbPager.setCurrentItem(index);
    }

    public void hideSearchBar() {
        binding.ssbTitle.requestFocus();
        binding.ssbSearch.clearFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int now = binding.ssbPager.getCurrentItem();
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