package com.example.schoolairdroprefactoredition.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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

    private TextView mName1;
    private TextView mName2;
    private int lastStack = 0;

    private boolean flag = true;// 用于标识需要进行变换的标题
    private boolean actionLock = true;// false 时为锁定,true 为释放
    private String lastName;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);

        settingName = getResources().getString(R.string.setting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        mName1 = findViewById(R.id.old);
        mName2 = findViewById(R.id.newcomer);
        mName1.setText(settingName);

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new SettingsFragment(), settingName)
                .commit();


        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (actionLock) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0)
                finish();
            else
                getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (actionLock && id == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0)
                finish();
            else
                getSupportFragmentManager().popBackStack();
            return true;
        } else
            return false;
    }

    @Override
    public void onBackStackChanged() {
        actionLock = false;

        TextView alpha = flag ? mName1 : mName2;
        TextView translation = mName2 == alpha ? mName1 : mName2;

        int nowStack = getSupportFragmentManager().getBackStackEntryCount();

        String now = getSupportFragmentManager().findFragmentById(R.id.container).getTag();

        if (lastStack < nowStack) { // 开启新的fragment
            translation.setText(now);

            Animation animA = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
            animA.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    alpha.setVisibility(View.INVISIBLE);
                    actionLock = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            Animation animT = AnimationUtils.loadAnimation(this, R.anim.enter_text);
            animT.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    translation.setVisibility(View.VISIBLE);
                    actionLock = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            alpha.startAnimation(animA);
            translation.startAnimation(animT);
        } else { // 关闭fragment
            flag = !flag;
            translation.setText(lastName);
            alpha.setText(now);

            Animation animA = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
            animA.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    alpha.setVisibility(View.VISIBLE);
                    actionLock = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            Animation animT = AnimationUtils.loadAnimation(this, R.anim.exit_text);
            animT.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    translation.setVisibility(View.INVISIBLE);
                    actionLock = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            alpha.startAnimation(animA);
            translation.startAnimation(animT);

        }

        flag = !flag;// 交换职责
        lastStack = nowStack;
        lastName = now;
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
    public static class SettingsNotificationFragment extends Fragment implements View.OnClickListener {

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

            mReceiveNew.setOnClickListener(this);
            mReceiveInApp.setOnClickListener(this);
            mVibrate.setOnClickListener(this);
            mSounds.setOnClickListener(this);

            return root;
        }


        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.settings_notification_receive:
                    mReceiveNew.toggle();
                    break;
                case R.id.settings_notification_in_app:
                    mReceiveInApp.toggle();
                    break;
                case R.id.settings_notification_vibrate:
                    mVibrate.toggle();
                    break;
                case R.id.settings_notification_sounds:
                    mSounds.toggle();
                    break;
            }
        }
    }

    // 隐私
    public static class SettingsPrivacyFragment extends Fragment implements View.OnClickListener {

        private PageItem mAddViaAlipay;
        private PageItem mRecommendFriends;
        private PageItem mBlackList;

        public SettingsPrivacyFragment() {
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_privacy, container, false);

            mAddViaAlipay = root.findViewById(R.id.settings_privacy_find_via_alipay);
            mRecommendFriends = root.findViewById(R.id.settings_privacy_recommend_alipay_friend);
            mBlackList = root.findViewById(R.id.settings_privacy_blacklist);

            mAddViaAlipay.setOnClickListener(this);
            mRecommendFriends.setOnClickListener(this);
            mBlackList.setOnClickListener(this);

            return root;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.settings_privacy_find_via_alipay)
                mAddViaAlipay.toggle();
            else if (id == R.id.settings_privacy_recommend_alipay_friend)
                mRecommendFriends.toggle();
            else if (id == R.id.settings_privacy_blacklist) {
                // open blacklist
            }
        }
    }

    /**
     * 通用
     */
    public static class SettingsGeneralFragment extends Fragment implements View.OnClickListener {

        private String languageName;

        private FragmentManager manager;

        public SettingsGeneralFragment() {
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);

            languageName = getResources().getString(R.string.language);

            if (getActivity() != null)
                manager = getActivity().getSupportFragmentManager();
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_general, container, false);

            root.findViewById(R.id.settings_general_language).setOnClickListener(this);
            root.findViewById(R.id.settings_general_storage).setOnClickListener(this);

            return root;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.settings_general_language)
                manager.beginTransaction()
                        // 这四个参数的意思分别是
                        // 1 新fragment进入动画
                        // 2 旧fragment退出动画
                        // 3 在新fragment回退时旧fragment的进入动画
                        // 4 在新fragment回退时新fragment的退出动画
                        .setCustomAnimations(R.anim.enter_y_fragment, R.anim.exit_y_fragment, R.anim.popenter_y_fragment, R.anim.popexit_y_fragment)
                        .replace(((ViewGroup) getView().getParent()).getId(), new SettingsLanguageFragment(), languageName)
                        .addToBackStack(languageName)
                        .commit();
        }
    }

    /**
     * 语言
     */
    public static class SettingsLanguageFragment extends Fragment implements View.OnClickListener {
        private ViewGroup mParent;
        private PageItem mSimplifiedCN;
        private PageItem mEN;

        public SettingsLanguageFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_language, container, false);

            mParent = root.findViewById(R.id.settings_language_parent);
            mSimplifiedCN = root.findViewById(R.id.settings_language_cn);
            mEN = root.findViewById(R.id.settings_language_en);

            mSimplifiedCN.setOnClickListener(this);
            mEN.setOnClickListener(this);

            return root;
        }

        private void deSelectAll() {
            for (int i = 0; i < mParent.getChildCount(); i++) {
                PageItem item = (PageItem) mParent.getChildAt(i);
                item.deSelect();
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.settings_language_cn) {
                if (!((PageItem) v).isItemSelected()) {
                    deSelectAll();
                }
                mSimplifiedCN.toggle();
            } else if (id == R.id.settings_language_en) {
                if (!((PageItem) v).isItemSelected()) {
                    deSelectAll();
                }
                mEN.toggle();
            }
        }
    }

    // 黑名单
    public static class SettingsBlacklistFragment extends Fragment {

        public SettingsBlacklistFragment() {
        }

        @Override
        public void onAttach(@NonNull Context context) {

            super.onAttach(context);
        }
    }

}