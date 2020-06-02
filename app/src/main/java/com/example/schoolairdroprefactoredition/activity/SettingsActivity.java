package com.example.schoolairdroprefactoredition.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.components.PageItem;

public class SettingsActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private String settingName;

    private TextView mFragmentName;
    private int stackNum = 0;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);

        settingName = getResources().getString(R.string.setting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        mFragmentName = findViewById(R.id.textView);

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment(), settingName)
                .commit();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0)
                finish();
            else
                getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        mFragmentName.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_text));
        Fragment now = getSupportFragmentManager().findFragmentById(R.id.settings);
        if (now != null)
            mFragmentName.setText(now.getTag());
    }

    /**
     * 设置的主页面
     */
    public static class SettingsFragment extends Fragment implements View.OnClickListener {
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

        public SettingsFragment() {
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);

            notificationName = getResources().getString(R.string.setting);
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

            mAlipay.setOnClickListener(this);
            mPrivacy.setOnClickListener(this);
            mNotification.setOnClickListener(this);
            mGeneral.setOnClickListener(this);
            mAbout.setOnClickListener(this);

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
                    break;
                case R.id.settings_home_notification:
                    // notification
                    manager.beginTransaction()
                            // 这四个参数的意思分别是
                            // 1 新fragment进入动画
                            // 2 旧fragment退出动画
                            // 3 在新fragment回退时旧fragment的进入动画
                            // 4 在新fragment回退时新fragment的退出动画
                            .setCustomAnimations(R.anim.enter_fragment, R.anim.popexit_fragment, R.anim.popenter_fragment, R.anim.exit_fragment)
                            .replace(((ViewGroup) getView().getParent()).getId(), new SettingsNotificationFragment(), notificationName)
                            .addToBackStack(null)
                            .commit();

                    break;
                case R.id.settings_home_general:
                    // general
                    manager.beginTransaction()
                            .setCustomAnimations(R.anim.enter_fragment, R.anim.popexit_fragment, R.anim.popenter_fragment, R.anim.exit_fragment)
                            .replace(((ViewGroup) getView().getParent()).getId(), new SettingsGeneralFragment(), generalName)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.settings_home_about:
                    // about
                    break;
            }
        }
    }

    /**
     * 关于页面
     */
    public static class SettingsAboutFragment extends Fragment {

        public SettingsAboutFragment() {
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    /**
     * 通知页面
     */
    public static class SettingsNotificationFragment extends Fragment {

        public SettingsNotificationFragment() {
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
        }

        private PageItem mReceiveNew;
        private PageItem mReceiveInApp;
        private PageItem mVibrate;
        private PageItem mSounds;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_notification, container, false);

            mReceiveNew = root.findViewById(R.id.settings_notification_receive);
            mReceiveInApp = root.findViewById(R.id.settings_notification_in_app);
            mVibrate = root.findViewById(R.id.settings_notification_vibrate);
            mSounds = root.findViewById(R.id.settings_notification_sounds);

            return root;
        }
    }

    public static class SettingsGeneralFragment extends Fragment {
        public SettingsGeneralFragment() {
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_general, container, false);
            return root;
        }
    }

}