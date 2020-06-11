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
import com.example.schoolairdroprefactoredition.ui.components.PageItem;

/**
 * 设置的主页面
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {
    private String notificationName;
    private String alipayBindingName;
    private String privacyName;
    private String generalName;
    private String aboutName;

    private FragmentManager manager;
    private PageItem mAlipay;
    private PageItem mPrivacy;
    private PageItem mNotification;
    private PageItem mGeneral;
    private PageItem mAbout;
    private TextView mSwitchAccount;
    private TextView mSignOut;

    public SettingsFragment() {
    }

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
        View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_home, container, false);

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
                // privacy
                manager.beginTransaction()
                        // 这四个参数的意思分别是
                        // 1 新fragment进入动画
                        // 2 旧fragment退出动画
                        // 3 在新fragment回退时旧fragment的进入动画
                        // 4 在新fragment回退时新fragment的退出动画

                        .setCustomAnimations(R.anim.enter_x_fragment, R.anim.exit_x_fragment, R.anim.popenter_x_fragment, R.anim.popexit_x_fragment)
                        .replace(((ViewGroup) getView().getParent()).getId(), new SettingsPrivacyFragment(), privacyName)
                        .addToBackStack(privacyName)
                        .commit();
                break;
            case R.id.settings_home_notification:
                // notification
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_x_fragment, R.anim.exit_x_fragment, R.anim.popenter_x_fragment, R.anim.popexit_x_fragment)
                        .replace(((ViewGroup) getView().getParent()).getId(), new SettingsNotificationFragment(), notificationName)
                        .addToBackStack(notificationName)
                        .commit();
                break;
            case R.id.settings_home_general:
                // general
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_x_fragment, R.anim.exit_x_fragment, R.anim.popenter_x_fragment, R.anim.popexit_x_fragment)
                        .replace(((ViewGroup) getView().getParent()).getId(), new SettingsGeneralFragment(), generalName)
                        .addToBackStack(generalName)
                        .commit();
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