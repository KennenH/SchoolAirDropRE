package com.example.schoolairdroprefactoredition.activity.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.TransactionBaseActivity;
import com.example.schoolairdroprefactoredition.fragment.settings.SettingsFragment;

public class SettingsActivity extends TransactionBaseActivity implements FragmentManager.OnBackStackChangedListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final String settingName = getResources().getString(R.string.setting);
        firstTransact(new SettingsFragment(), settingName);
    }
}