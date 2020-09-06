package com.example.schoolairdroprefactoredition.scene.settings;

import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.schoolairdroprefactoredition.scene.base.TransactionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.main.my.user.UserFragment;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

public class UserActivity extends TransactionBaseActivity implements FragmentManager.OnBackStackChangedListener {

    public static void start(Context context,Bundle bundle) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(ConstantUtil.KEY_AUTHORIZE, bundle);
        context.startActivity(intent);
    }

    private Bundle bundle;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        firstTransact(new UserFragment());
    }
}
