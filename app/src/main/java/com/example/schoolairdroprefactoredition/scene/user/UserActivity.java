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

import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.TransactionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.user.fragment.UserFragment;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

public class UserActivity extends TransactionBaseActivity implements FragmentManager.OnBackStackChangedListener {
    public static final int REQUEST_UPDATE = 520;

    /**
     * @param isModifiable 信息是否可修改 只有在
     *                     {@link com.example.schoolairdroprefactoredition.scene.main.my.MyFragment}
     *                     中进入自己的个人信息页才可修改
     */
    public static void startForResult(Context context, Bundle bundle, boolean isModifiable) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtras(bundle);
        intent.putExtra(ConstantUtil.KEY_INFO_MODIFIABLE, isModifiable);
        ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_UPDATE);
    }

    private OnUserInfoUpdatedListener mOnUserInfoUpdatedListener;

    private Bundle bundle;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bundle = getIntent().getExtras();

        firstTransact(UserFragment.newInstance(bundle));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null)
                if (requestCode == REQUEST_UPDATE) {
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
