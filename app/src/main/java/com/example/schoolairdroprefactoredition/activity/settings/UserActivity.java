package com.example.schoolairdroprefactoredition.activity.settings;

import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.TransactionBaseActivity;
import com.example.schoolairdroprefactoredition.fragment.user.UserFragment;

public class UserActivity extends TransactionBaseActivity implements FragmentManager.OnBackStackChangedListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, UserActivity.class);
        context.startActivity(intent);
    }

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String userInfoName = getResources().getString(R.string.userInfo);
        mName1.setText(userInfoName);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new UserFragment(), userInfoName)
                .commit();
    }
}
