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

import com.example.schoolairdroprefactoredition.domain.DomainToken;
import com.example.schoolairdroprefactoredition.domain.base.DomainBaseUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.TransitionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.user.fragment.UserFragment;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import java.lang.reflect.InvocationTargetException;

import javadz.beanutils.BeanUtils;

public class UserActivity extends TransitionBaseActivity implements FragmentManager.OnBackStackChangedListener {
    public static final int REQUEST_UPDATE = 520;

    /**
     * 两个参数的Bean类需要 直接 包含用户基本信息的域和get set方法
     *
     * @param isMyOwnPageAndModifiable 是否可修改
     *                                 只有在{@link com.example.schoolairdroprefactoredition.scene.main.my.MyFragment}
     *                                 中进入自己的个人信息页才可修改
     * @param token                    验证信息
     * @param thisPersonInfo           这个人的信息
     */
    public static void start(Context context, boolean isMyOwnPageAndModifiable, DomainToken token, Object thisPersonInfo) {
        if (thisPersonInfo == null) return;

        DomainBaseUserInfo thisPerson = new DomainBaseUserInfo();
        try {
            BeanUtils.copyProperties(thisPerson, thisPersonInfo);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(ConstantUtil.KEY_TOKEN, token);
        intent.putExtra(ConstantUtil.KEY_USER_INFO, thisPerson);
        intent.putExtra(ConstantUtil.KEY_INFO_MODIFIABLE, isMyOwnPageAndModifiable);
        if (context instanceof AppCompatActivity)
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
                                .onUpdated(bundle.getSerializable(ConstantUtil.KEY_USER_INFO));
                    }
                }
        }
    }

    public interface OnUserInfoUpdatedListener {
        void onUpdated(Object info);
    }

    public void setOnUserInfoUpdateListener(OnUserInfoUpdatedListener listener) {
        mOnUserInfoUpdatedListener = listener;
    }
}
