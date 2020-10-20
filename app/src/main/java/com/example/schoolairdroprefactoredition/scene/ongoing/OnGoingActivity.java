package com.example.schoolairdroprefactoredition.scene.ongoing;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ActivityTabBinding;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.OnGoingPagerAdapter;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

public class OnGoingActivity extends ImmersionStatusBarActivity {

    private ActivityTabBinding binding;

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, OnGoingActivity.class);
        intent.putExtra(ConstantUtil.KEY_AUTHORIZE, bundle);
        context.startActivity(intent);
    }

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        binding = ActivityTabBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar(findViewById(R.id.toolbar));
        binding.name.setText(R.string.onGoing);

        binding.pager.setAdapter(new OnGoingPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this));
        binding.tab.setTabTextColors(getResources().getColor(R.color.black, getTheme()), getResources().getColor(R.color.black, getTheme()));
        binding.tab.setTabIndicatorFullWidth(false);
        binding.tab.setupWithViewPager(binding.pager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}