package com.example.schoolairdroprefactoredition.scene.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainGetUserInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.scene.settings.fragment.SettingsViewModel;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, BaseChildFragmentViewModel.OnRequestListener {

    public static final int LOGIN = 1212;// 请求码 网络登录请求

    /**
     * 尚未登录时
     */
    public static void startForLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).startActivityForResult(intent, LOGIN);
            ((AppCompatActivity) context).overridePendingTransition(R.anim.enter_y_fragment, R.anim.alpha_out);
        }
    }

    /**
     * 已登录时
     */
    public static void startAfterLogin(Context context, DomainGetUserInfo.DataBean userInfo) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(ConstantUtil.KEY_USER_INFO, userInfo);
        context.startActivity(intent);
    }

    private CheckBox mCheck;
    private TextView mLogin;
    private TextView mCancel;

    private SettingsViewModel viewModel;

    private LoadingPopupView mLoading;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DomainGetUserInfo.DataBean info = (DomainGetUserInfo.DataBean) getIntent().getSerializableExtra(ConstantUtil.KEY_USER_INFO);

        // 已登录时
        if (info != null) {
            setContentView(R.layout.activity_logged_in);

            final TextView alipay = findViewById(R.id.user_name);
            final ImageView close = findViewById(R.id.close);

            final String phone = info.getUalipay();
            final String priPhone = phone.substring(0, 3).concat("****").concat(phone.substring(7));

            final boolean[] isPri = {true};

            alipay.setText(priPhone);
            alipay.setOnClickListener(v -> {
                alipay.setText(isPri[0] ? phone : priPhone);
                isPri[0] = !isPri[0];
            });

            close.setOnClickListener(v -> {
                finish();
                overrideAnimation();
            });


        }
        // 尚未登录
        else {
            setContentView(R.layout.activity_login);
            viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
            viewModel.setOnRequestListener(this);

            mCheck = findViewById(R.id.checkbox);
            mLogin = findViewById(R.id.login_with_alipay);
            mCancel = findViewById(R.id.cancel);

            mLogin.setEnabled(false);
            mCancel.setOnClickListener(this);
            mCheck.setOnCheckedChangeListener(this);
            mCheck.setSelected(false);

            mLogin.setOnClickListener(this);

            mLoading = new XPopup.Builder(this).asLoading();

            intent = getIntent();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
        overrideAnimation();
    }

    private void overrideAnimation() {
        overridePendingTransition(0, R.anim.popexit_y_fragment);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login_with_alipay) {
            // 请求公钥与sessionID
            showPopup();

            viewModel.getPublicKey().observe(this, key -> {
                // 将加密的数据post回去请求token
                viewModel.authorizeWithAlipayID(key.getCookie()
                        , "client_credentials"
                        , "testclient"
                        , "123456"
                        , "19858120611"
                        , key.getPublic_key()).observe(this, token -> {
                    dismissPopup();

                    // token
                    intent.putExtra(ConstantUtil.KEY_AUTHORIZE, token);

                    if (mLoading != null) mLoading.show();
                    viewModel.getUserInfo(token.getAccess_token()).observe(this, info -> {
                        dismissPopup();

                        DomainGetUserInfo.DataBean userInfo = info.getData().get(0);
                        // token换取的user info
                        intent.putExtra(ConstantUtil.KEY_USER_INFO, userInfo);

                        setResult(Activity.RESULT_OK, intent);

                        finish();
                        overrideAnimation();
                    });
                });
            });


        } else if (id == R.id.cancel) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            overrideAnimation();
        }
    }

    /**
     * 取消加载动画
     */
    private void dismissPopup() {
        if (mLoading != null) mLoading.dismiss();
    }

    /**
     * 加载动画
     */
    private void showPopup() {
        if (mLoading != null) mLoading.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mLogin.setEnabled(isChecked);
    }

    @Override
    public void onError() {
        dismissPopup();
    }

    @Override
    public void onLoading() {
    }
}