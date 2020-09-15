package com.example.schoolairdroprefactoredition.scene.settings;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;

public class LoginActivity extends ImmersionStatusBarActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, BaseStateViewModel.OnRequestListener {

    public static final int LOGIN = 1212;// 请求码 网络登录请求

    /**
     * 尚未登录时
     */
    public static void startForLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).startActivityForResult(intent, LOGIN);
            MyUtil.startAnimUp((AppCompatActivity) context);
        }
    }

    /**
     * 已登录时
     */
    public static void startAfterLogin(Context context, DomainUserInfo.DataBean userInfo) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(ConstantUtil.KEY_USER_INFO, userInfo);
        context.startActivity(intent);
        if (context instanceof AppCompatActivity)
            MyUtil.startAnimUp((AppCompatActivity) context);
    }

    private CheckBox mCheck;
    private TextView mLogin;
    private TextView mCancel;

    private SettingsViewModel viewModel;

    private LoadingPopupView mLoading;

    private Intent intent;

    private NetworkUtils.OnNetworkStatusChangedListener networkStatusListener = new NetworkUtils.OnNetworkStatusChangedListener() {
        @Override
        public void onDisconnected() {
            showToast(getString(R.string.connectionLost));
            dismissPopup();
        }

        @Override
        public void onConnected(NetworkUtils.NetworkType networkType) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DomainUserInfo.DataBean info = (DomainUserInfo.DataBean) getIntent().getSerializableExtra(ConstantUtil.KEY_USER_INFO);

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
                MyUtil.exitAnimDown(this);
            });

            ClickUtils.applyPressedViewAlpha(close, 0.6f);
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
        MyUtil.exitAnimDown(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login_with_alipay) {
            // 请求公钥与sessionID
            showPopup();

            if (NetworkUtils.isConnected()) {
                NetworkUtils.registerNetworkStatusChangedListener(networkStatusListener);
                loginWithAlipay();
            } else {
                showToast(getString(R.string.networkUnavailable));
                dismissPopup();
            }

        } else if (id == R.id.cancel) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            MyUtil.exitAnimDown(this);
        }
    }

    /**
     * 登录网络请求
     * 两次请求获取token信息
     * 最后使用token换取用户信息
     */
    private void loginWithAlipay() {
        viewModel.getPublicKey().observe(this, key -> {
            // 将加密的数据post回去请求token
            viewModel.authorizeWithAlipayID(key.getCookie()
                    , "client_credentials"
                    , "testclient"
                    , "123456"
                    , "19858120611"
                    , key.getPublic_key()).observe(this, token -> {
                dismissPopup();

                LogUtils.d("token -- > " + token.toString());

                // token
                intent.putExtra(ConstantUtil.KEY_AUTHORIZE, token);

                getUserInfoWithToken(token);
            });
        });
    }

    /**
     * 使用token获取用户信息
     *
     * @param token token
     */
    private void getUserInfoWithToken(DomainAuthorize token) {
        showPopup();
        viewModel.getUserInfo(token.getAccess_token()).observe(this, info -> {
            DomainUserInfo.DataBean userInfo = info.getData().get(0);
            // token换取的user info
            intent.putExtra(ConstantUtil.KEY_USER_INFO, userInfo);

            setResult(Activity.RESULT_OK, intent);
            NetworkUtils.unregisterNetworkStatusChangedListener(networkStatusListener);

            dismissPopup();

            finish();
            MyUtil.exitAnimDown(this);
        });
    }

    private void showToast(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
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