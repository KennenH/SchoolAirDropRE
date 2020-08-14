package com.example.schoolairdroprefactoredition.activity.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.TransactionBaseActivity;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.fragment.settings.SettingsFragment;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil;

import java.io.Serializable;

import static com.example.schoolairdroprefactoredition.utils.ConstantUtil.KEY_AUTHORIZE;

public class SettingsActivity extends TransactionBaseActivity implements FragmentManager.OnBackStackChangedListener {

    public static final int REQUEST_LOGIN = 1212; // 请求码 登陆

    public Bundle bundle;

    public static void startForResult(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        if (context instanceof AppCompatActivity)
            ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_LOGIN);
    }


    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final String settingName = getResources().getString(R.string.setting);
        firstTransact(SettingsFragment.newInstance(bundle), settingName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                if (data != null)
                    bundle.putSerializable(ConstantUtil.KEY_AUTHORIZE, data.getSerializableExtra(KEY_AUTHORIZE));
            }
        }
    }
}