package com.example.schoolairdroprefactoredition.scene.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.TransactionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.user.fragment.UserFragment;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

public class UserActivity extends TransactionBaseActivity implements FragmentManager.OnBackStackChangedListener {
    public static final int REQUEST_UPDATE = 520;

    public static void startForResult(Context context, Bundle bundle) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtras(bundle);
        ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_UPDATE);
    }

    private OnUserInfoUpdatedListener mOnUserInfoUpdatedListener;

    private Bundle bundle;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        firstTransact(UserFragment.newInstance(bundle));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null)
                if (requestCode == REQUEST_UPDATE) {
                    LogUtils.d("user info modified");

                    data.putExtra(ConstantUtil.KEY_UPDATED, true);
                    setResult(Activity.RESULT_OK, data);

                    bundle = data.getExtras();
                    if (bundle != null) {
                        mOnUserInfoUpdatedListener
                                .onUpdated((DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO));
                    }
                }
        }
    }

    public interface OnUserInfoUpdatedListener {
        void onUpdated(DomainUserInfo.DataBean info);
    }

    public void setOnUserInfoUpdateListener(OnUserInfoUpdatedListener listener) {
        mOnUserInfoUpdatedListener = listener;
    }
}
