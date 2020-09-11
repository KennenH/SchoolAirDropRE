package com.example.schoolairdroprefactoredition.scene.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.scene.base.TransactionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.user.user.UserFragment;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

public class UserActivity extends TransactionBaseActivity implements FragmentManager.OnBackStackChangedListener {

    public static final int USER_MODIFY = 520;

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startForResult(Context context, Bundle bundle) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtras(bundle);
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).startActivityForResult(intent, 520, intent.getExtras());
        }
    }

    private Bundle bundle;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        firstTransact(UserFragment.newInstance(bundle));
    }
}
