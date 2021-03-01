package com.example.schoolairdroprefactoredition.scene.settings.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentSettingsHomeBinding;
import com.example.schoolairdroprefactoredition.domain.DomainToken;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.TransitionBaseFragment;
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity;
import com.example.schoolairdroprefactoredition.scene.settings.SettingsActivity;
import com.example.schoolairdroprefactoredition.scene.switchaccount.SwitchAccountActivity;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DialogUtil;
import com.example.schoolairdroprefactoredition.viewmodel.LoginViewModel;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;

/**
 * 设置的主页面
 */
public class SettingsFragment extends TransitionBaseFragment implements View.OnClickListener, SettingsActivity.OnLoginListener {
    public static final int LOGOUT = 1205; // 请求码 退出本地登录

    private LoginViewModel loginViewModel;

    private FragmentManager manager;

    private FragmentSettingsHomeBinding binding;

    private String notificationName;
    private String privacyName;
    private String generalName;
    private String aboutName;

    private LoadingPopupView mLoading;

    private Bundle bundle;
    private DomainUserInfo.DataBean userInfo = null;
    private DomainToken token = null;

    public static SettingsFragment newInstance(Intent intent) {
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(intent.getExtras());
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        notificationName = getResources().getString(R.string.notification);
        privacyName = getResources().getString(R.string.privacy);
        generalName = getResources().getString(R.string.general);
        aboutName = getResources().getString(R.string.about);

        if (getActivity() != null) {
            manager = getActivity().getSupportFragmentManager();
        }
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
            token = (DomainToken) bundle.getSerializable(ConstantUtil.KEY_TOKEN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsHomeBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_home, container, false));
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.settingsHomeAlipay.setOnClickListener(this);
        binding.settingsHomePrivacy.setOnClickListener(this);
        binding.settingsHomeNotification.setOnClickListener(this);
        binding.settingsHomeGeneral.setOnClickListener(this);
        binding.settingsHomeAbout.setOnClickListener(this);
        binding.settingsHomeSwitchAccount.setOnClickListener(this);
        binding.settingsHomeSignOut.setOnClickListener(this);

        mLoading = new XPopup.Builder(getContext()).asLoading();

        validateState();

        return binding.getRoot();
    }

    /**
     * {@link SettingsActivity}登录监听回调
     */
    @Override
    public void onLogged(Intent intent) {
        bundle = intent.getExtras();
        if (bundle != null) {
            userInfo = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
            token = (DomainToken) bundle.getSerializable(ConstantUtil.KEY_TOKEN);
            validateState();
        }
    }

    /**
     * 有效化登录状态或退登状态
     * 使用bundle填充页面用户信息或清除页面数据
     */
    private void validateState() {
        if (isLoggedIn()) {
            binding.settingsHomeAlipay.setDescription(getString(R.string.loggedIn, userInfo.getUserName()));
            binding.settingsHomeSwitchAccount.setVisibility(View.VISIBLE);
            binding.settingsHomeSignOut.setVisibility(View.VISIBLE);
        } else {
            binding.settingsHomePrivacy.setVisibility(View.GONE);
            binding.settingsHomeNotification.setVisibility(View.GONE);
            binding.settingsHomeSwitchAccount.setVisibility(View.GONE);
            binding.settingsHomeSignOut.setVisibility(View.GONE);
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
        if (id == R.id.settings_home_alipay) {
            LoginActivity.Companion.start(getContext());
            if (getActivity() != null) {
                getActivity().overridePendingTransition(R.anim.enter_y_fragment, R.anim.alpha_out);
            }
        } else if (id == R.id.settings_home_privacy) {
            transact(manager, SettingsPrivacyFragment.newInstance(bundle), privacyName);
        } else if (id == R.id.settings_home_notification) {
            transact(manager, SettingsNotificationFragment.newInstance(bundle), notificationName);
        } else if (id == R.id.settings_home_general) {
            transact(manager, SettingsGeneralFragment.Companion.newInstance(bundle), generalName);
        } else if (id == R.id.settings_home_about) {
            transact(manager, new SettingsAboutFragment(), aboutName);
        } else if (id == R.id.settings_home_switch_account) {
            // switch account
            if (getActivity() != null && getContext() != null) {
                SwitchAccountActivity.Companion.start(getContext(), getActivity().getIntent().getExtras());
            }
        } else if (id == R.id.settings_home_sign_out) {
            if (getContext() != null) {
                DialogUtil.showConfirm(getContext(), getString(R.string.logout), getString(R.string.confirmLogout),
                        () -> {
                            loginViewModel.logout();
                            if (getActivity() != null) {
                                getActivity().setResult(Activity.RESULT_OK, null);
                                getActivity().finish();
                            }
                        }
                );
            }
        }
    }
}