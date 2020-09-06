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
import com.example.schoolairdroprefactoredition.domain.DomainGetUserInfo;
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity;
import com.example.schoolairdroprefactoredition.scene.base.TransactionBaseFragment;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.scene.settings.SettingsActivity;
import com.example.schoolairdroprefactoredition.ui.components.PageItem;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;

/**
 * 设置的主页面
 */
public class SettingsFragment extends TransactionBaseFragment implements View.OnClickListener, BaseChildFragmentViewModel.OnRequestListener, SettingsActivity.OnLoginListener {

    private SettingsViewModel settingsViewModel;

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
    private TextView mSignOut;

    private LoadingPopupView mLoading;

    private Bundle bundle;
    private DomainGetUserInfo.DataBean userInfo = null;
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
            userInfo = (DomainGetUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
            token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_home, container, false);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.setOnRequestListener(this);

        mAlipay = root.findViewById(R.id.settings_home_alipay);
        mPrivacy = root.findViewById(R.id.settings_home_privacy);
        mNotification = root.findViewById(R.id.settings_home_notification);
        mGeneral = root.findViewById(R.id.settings_home_general);
        mAbout = root.findViewById(R.id.settings_home_about);
        mSwitchAccount = root.findViewById(R.id.settings_home_switch_account);
        mSignOut = root.findViewById(R.id.settings_home_sign_out);

        mAlipay.setOnClickListener(this);
        mPrivacy.setOnClickListener(this);
        mNotification.setOnClickListener(this);
        mGeneral.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mSwitchAccount.setOnClickListener(this);
        mSignOut.setOnClickListener(this);

        mLoading = new XPopup.Builder(getContext()).asLoading();

        init();

        return root;
    }

    /**
     * {@link SettingsActivity}登录监听回调
     */
    @Override
    public void onLogged(Intent intent) {
        bundle = intent.getExtras();
        if (bundle != null) {
            userInfo = (DomainGetUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
            if (userInfo != null)
                mAlipay.setDescription(getString(R.string.loggedIn, userInfo.getUname()));
        }
    }

    /**
     * 使用bundle填充页面用户信息
     */
    private void init() {
        if (userInfo != null) {
            mAlipay.setDescription(getString(R.string.loggedIn, userInfo.getUname()));
        }
    }

    /**
     * 是否已登录
     *
     * @return 是否登登录
     */
    private boolean isLoggedIn() {
        return userInfo != null
                || token != null;
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
                transact(manager, SettingsGeneralFragment.newInstance(bundle), generalName);
                break;
            case R.id.settings_home_about:
                transact(manager, new SettingsAboutFragment(), aboutName);
                break;
            case R.id.settings_home_switch_account:
                // switch account
                break;
            case R.id.settings_home_sign_out:
                // sign out and delete all relative user info on this device
                break;
        }
    }

    @Override
    public void onError() {
        if (mLoading != null)
            mLoading.dismiss();
    }

    @Override
    public void onLoading() {
        if (mLoading != null)
            mLoading.dismiss();
    }
}