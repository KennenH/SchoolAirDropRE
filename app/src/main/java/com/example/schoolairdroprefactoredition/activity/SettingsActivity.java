package com.example.schoolairdroprefactoredition.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.fragment.settings.SettingsFragment;

public class SettingsActivity extends TransactionBaseActivity implements FragmentManager.OnBackStackChangedListener {
    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String settingName = getResources().getString(R.string.setting);
        mName1.setText(settingName);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new SettingsFragment(), settingName)
                .commit();
    }
}