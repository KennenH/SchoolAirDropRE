package com.example.schoolairdroprefactoredition.scene.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ActivityLoggedInBinding;
import com.example.schoolairdroprefactoredition.databinding.ActivityLoginBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;

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

    private ActivityLoginBinding binding;

    private LoginViewModel viewModel;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DomainUserInfo.DataBean info = (DomainUserInfo.DataBean) getIntent().getSerializableExtra(ConstantUtil.KEY_USER_INFO);

        // 已登录时
        if (info != null) {
            final ActivityLoggedInBinding binding = ActivityLoggedInBinding.inflate(LayoutInflater.from(this));
            setContentView(binding.getRoot());

            final String phone = info.getUalipay();
            final String priPhone = phone.substring(0, 3).concat("****").concat(phone.substring(7));

            final boolean[] isPri = {true};

            binding.userName.setText(priPhone);
            binding.userName.setOnClickListener(v -> {
                binding.userName.setText(isPri[0] ? phone : priPhone);
                isPri[0] = !isPri[0];
            });

            binding.close.setOnClickListener(v -> {
                finish();
                MyUtil.exitAnimDown(this);
            });

            ClickUtils.applyPressedViewAlpha(binding.close, 0.6f);
        }
        // 尚未登录
        else {
            binding = ActivityLoginBinding.inflate(LayoutInflater.from(this));
            setContentView(binding.getRoot());
            viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
            viewModel.setOnRequestListener(this);

            binding.loginWithAlipay.setEnabled(false);
            binding.cancel.setOnClickListener(this);
            binding.checkbox.setOnCheckedChangeListener(this);
            binding.checkbox.setSelected(false);
            binding.loginWithAlipay.setOnClickListener(this);

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
            showLoading();
            if (NetworkUtils.isConnected()) {
                loginWithAlipay();
            } else {
                MyUtil.showCenterDialog(this, MyUtil.DIALOG_TYPE.ERROR_NETWORK);
                dismissLoading();
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
                    , "19858120611"
                    , key.getPublic_key()).observe(this, token -> {
                dismissLoading();

                // todo comment this when release
                LogUtils.d("token -- > " + token.toString());

                // token
                intent.putExtra(ConstantUtil.KEY_AUTHORIZE, token);
                getUserInfoWithToken();
            });
        });
    }

    /**
     * 使用token获取用户信息
     */
    private void getUserInfoWithToken() {
        DomainAuthorize token = (DomainAuthorize) getIntent().getSerializableExtra(ConstantUtil.KEY_AUTHORIZE);
        if (token != null) {
            showLoading();
            viewModel.getUserInfo(token.getAccess_token()).observe(this, info -> {
                DomainUserInfo.DataBean userInfo = info.getData().get(0);
                // token换取的user info
                intent.putExtra(ConstantUtil.KEY_USER_INFO, userInfo);

                setResult(Activity.RESULT_OK, intent);
                dismissLoading();

                finish();
                MyUtil.exitAnimDown(this);
            });
        } else MyUtil.showCenterDialog(this, MyUtil.DIALOG_TYPE.ERROR_UNKNOWN);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        binding.loginWithAlipay.setEnabled(isChecked);
    }

    @Override
    public void onError() {
        dismissLoading();
        MyUtil.showCenterDialog(this, MyUtil.DIALOG_TYPE.ERROR_UNKNOWN);
    }

}