package com.example.schoolairdroprefactoredition.fragment.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.fragment.TransactionBaseFragment;
import com.example.schoolairdroprefactoredition.ui.components.PageItem;

import org.jetbrains.annotations.NotNull;

/**
 * 设置的主页面
 */
public class SettingsFragment extends TransactionBaseFragment implements View.OnClickListener {
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        notificationName = getResources().getString(R.string.notification);
        alipayBindingName = getResources().getString(R.string.alipayBinding);
        privacyName = getResources().getString(R.string.privacy);
        generalName = getResources().getString(R.string.general);
        aboutName = getResources().getString(R.string.about);

        if (getActivity() != null)
            manager = getActivity().getSupportFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_home, container, false);

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

        return root;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.settings_home_alipay:
                // alipay
                break;
            case R.id.settings_home_privacy:
                transact(manager,new SettingsPrivacyFragment(),privacyName);
                break;
            case R.id.settings_home_notification:
                transact(manager,new SettingsNotificationFragment(),notificationName);
                break;
            case R.id.settings_home_general:
                transact(manager,new SettingsGeneralFragment(),generalName);
                break;
            case R.id.settings_home_about:
                // about
                break;
            case R.id.settings_home_switch_account:
                // switch account
                break;
            case R.id.settings_home_sign_out:
                // sign out and delete all relative user info on this device
                break;
        }
    }
}