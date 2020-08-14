package com.example.schoolairdroprefactoredition.fragment.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.settings.LoginActivity;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.fragment.TransactionBaseFragment;
import com.example.schoolairdroprefactoredition.fragment.home.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.fragment.ssb.SoldFragment;
import com.example.schoolairdroprefactoredition.ui.components.PageItem;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;

/**
 * 设置的主页面
 */
public class SettingsFragment extends TransactionBaseFragment implements View.OnClickListener, BaseChildFragmentViewModel.OnRequestListener {

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

    public static SettingsFragment newInstance(Bundle bundle) {
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(bundle);
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
        bundle = getArguments();
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

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("SettingFragment", "result code == > " + resultCode + " request code == > " + requestCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LoginActivity.REQUEST_LOGIN) {
                // 请求公钥与sessionID
                if (mLoading != null)
                    mLoading.show();
                settingsViewModel.getPublicKey().observe(getViewLifecycleOwner(), key -> {
                    if (mLoading != null) mLoading.dismiss();

                    // 将加密的数据post回去请求token
                    if (mLoading != null)
                        mLoading.show();
                    settingsViewModel.authorizeWithAlipayID(key.getSession_id()
                            , "client_credentials"
                            , "testclient"
                            , "123456"
                            , "19858120611"
                            , key.getPublic_key())
                            .observe(getViewLifecycleOwner(), token -> {
                                if (mLoading != null) mLoading.dismiss();

                                Log.d("Authorization get", token.toString());
                            });
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.settings_home_alipay:
//                LoginActivity.startForResult(getContext());// 直接getActivity 会使结果返回至fragment的父Activity
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivityForResult(intent, LoginActivity.REQUEST_LOGIN);
                getActivity().overridePendingTransition(R.anim.enter_y_fragment, R.anim.alpha_out);
                break;
            case R.id.settings_home_privacy:
                transact(manager, new SettingsPrivacyFragment(), privacyName);
                break;
            case R.id.settings_home_notification:
                transact(manager, new SettingsNotificationFragment(), notificationName);
                break;
            case R.id.settings_home_general:
                transact(manager, new SettingsGeneralFragment(), generalName);
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