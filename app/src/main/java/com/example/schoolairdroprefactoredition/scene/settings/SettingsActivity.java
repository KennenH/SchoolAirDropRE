package com.example.schoolairdroprefactoredition.scene.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.base.TransactionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.settings.fragment.SettingsFragment;

public class SettingsActivity extends TransactionBaseActivity implements FragmentManager.OnBackStackChangedListener {

    public Intent intent;

    private OnLoginListener mOnLoginListener;

    public static void startForResultLogin(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SettingsActivity.class);
        if (bundle != null) intent.putExtras(bundle);
        if (context instanceof AppCompatActivity)
            ((AppCompatActivity) context).startActivityForResult(intent, LoginActivity.LOGIN);
    }

    public static void startForResultLogout(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SettingsActivity.class);
        if (bundle != null) intent.putExtras(bundle);
        if (context instanceof AppCompatActivity)
            ((AppCompatActivity) context).startActivityForResult(intent, SettingsFragment.LOGOUT);
    }

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intent = getIntent();

        final String settingName = getResources().getString(R.string.setting);
        firstTransact(SettingsFragment.newInstance(intent), settingName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LoginActivity.LOGIN) {
                setResult(Activity.RESULT_OK, data); // 将登录结果返回至MainActivity
                mOnLoginListener.onLogged(data);
            }
        }
    }

    /**
     * {@link SettingsFragment}监听的登录回调
     * 以便登陆后它立即获取用户信息来填充页面
     */
    public interface OnLoginListener {
        void onLogged(Intent intent);
    }

    public void setOnLoginListener(OnLoginListener listener) {
        mOnLoginListener = listener;
    }
}