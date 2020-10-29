package com.example.schoolairdroprefactoredition.scene.settings.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.TransactionBaseFragment;
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity;
import com.example.schoolairdroprefactoredition.scene.settings.LoginViewModel;
import com.example.schoolairdroprefactoredition.scene.settings.SettingsActivity;
import com.example.schoolairdroprefactoredition.ui.components.PageItem;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DialogUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

/**
 * 设置的主页面
 */
public class SettingsFragment extends TransactionBaseFragment implements View.OnClickListener, SettingsActivity.OnLoginListener, LoginViewModel.OnLoginErrorListener {
    public static final int LOGOUT = 1205; // 请求码 退出本地登录

    private LoginViewModel loginViewModel;

    private FragmentManager manager;

    private String notificationName;
    private String alipayBindingName;
    private String privacyName;
    private String generalName;
    private String aboutName;

    private PageItem mAlipay;
    private PageItem mPrivacy;
    private PageItem mNotification;
    private PageItem mGeneral;
    private PageItem mAbout;
    private TextView mSwitchAccount;
    private TextView mLogout;

    private LoadingPopupView mLoading;

    private Bundle bundle;
    private DomainUserInfo.DataBean userInfo = null;
    private DomainAuthorize token = null;

    public static SettingsFragment newInstance(Intent intent) {
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(intent.getExtras());
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        notificationName = getResources().getString(R.string.notification);
        alipayBindingName = getResources().getString(R.string.alipayAccountText);
        privacyName = getResources().getString(R.string.privacy);
        generalName = getResources().getString(R.string.general);
        aboutName = getResources().getString(R.string.about);

        if (getActivity() != null)
            manager = getActivity().getSupportFragmentManager();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof SettingsActivity) {
            ((SettingsActivity) getActivity()).setOnLoginListener(this);
        }
        bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        } else {
            userInfo = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
            token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_home, container, false);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.setOnLoginErrorListener(this);

        mAlipay = root.findViewById(R.id.settings_home_alipay);
        mPrivacy = root.findViewById(R.id.settings_home_privacy);
        mNotification = root.findViewById(R.id.settings_home_notification);
        mGeneral = root.findViewById(R.id.settings_home_general);
        mAbout = root.findViewById(R.id.settings_home_about);
        mSwitchAccount = root.findViewById(R.id.settings_home_switch_account);
        mLogout = root.findViewById(R.id.settings_home_sign_out);

        mAlipay.setOnClickListener(this);
        mPrivacy.setOnClickListener(this);
        mNotification.setOnClickListener(this);
        mGeneral.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mSwitchAccount.setOnClickListener(this);
        mLogout.setOnClickListener(this);

        mLoading = new XPopup.Builder(getContext()).asLoading();

        validateState();

        return root;
    }

    /**
     * {@link SettingsActivity}登录监听回调
     */
    @Override
    public void onLogged(Intent intent) {
        bundle = intent.getExtras();
        if (bundle != null) {
            userInfo = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
            token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
            validateState();
        }
    }

    /**
     * 有效化登录状态或退登状态
     * 使用bundle填充页面用户信息或清除页面数据
     */
    private void validateState() {
        if (isLoggedIn()) {
            mAlipay.setDescription(getString(R.string.loggedIn, userInfo.getUname()));
            mSwitchAccount.setVisibility(View.VISIBLE);
            mLogout.setVisibility(View.VISIBLE);
        } else {
            mSwitchAccount.setVisibility(View.GONE);
            mLogout.setVisibility(View.GONE);
        }
    }

    /**
     * 是否已登录
     *
     * @return 是否登登录
     */
    private boolean isLoggedIn() {
        return userInfo != null
                && token != null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.settings_home_alipay:
                if (!isLoggedIn())
                    LoginActivity.startForLogin(getContext());
                else
                    LoginActivity.startAfterLogin(getContext(), userInfo);

                if (getActivity() != null)
                    getActivity().overridePendingTransition(R.anim.enter_y_fragment, R.anim.alpha_out);

                break;
            case R.id.settings_home_privacy:
                transact(manager, SettingsPrivacyFragment.newInstance(bundle), privacyName);
                break;
            case R.id.settings_home_notification:
                transact(manager, SettingsNotificationFragment.newInstance(bundle), notificationName);
                break;
            case R.id.settings_home_general:
                transact(manager, SettingsGeneralFragment.Companion.newInstance(bundle), generalName);
                break;
            case R.id.settings_home_about:
                transact(manager, new SettingsAboutFragment(), aboutName);
                break;
            case R.id.settings_home_switch_account:
                // switch account
                break;
            case R.id.settings_home_sign_out:
                DialogUtil.showConfirm(getContext(), getString(R.string.logout), getString(R.string.confirmLogout),
                        () -> {
                            loginViewModel.logout();
                            if (getActivity() != null) {
                                getActivity().setResult(Activity.RESULT_OK);
                                getActivity().finish();
                            }
                        }
                );
                break;
        }
    }

    @Override
    public void onLoginError() {
        if (mLoading != null)
            mLoading.smartDismiss();

        DialogUtil.showCenterDialog(getContext(), DialogUtil.DIALOG_TYPE.FAILED, R.string.errorLogin);
    }
}