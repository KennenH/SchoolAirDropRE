package com.example.schoolairdroprefactoredition.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.components.PageItem;

public class SettingsActivity extends AppCompatActivity {
    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // 当在设置主页时结束activity
            // 否则从回退栈中弹出一个页面
            // ceshi
            Toast.makeText(this, "" + getSupportFragmentManager().getBackStackEntryCount(), Toast.LENGTH_SHORT).show();
            if (getSupportFragmentManager().getBackStackEntryCount() == 0)
                finish();
            else
                getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置的主页面
     */
    public static class SettingsFragment extends Fragment implements View.OnClickListener {
        private static final String NAME = "SettingsFragment";
        private PageItem mAlipay;
        private PageItem mPrivacy;
        private PageItem mNotification;
        private PageItem mGeneral;
        private PageItem mAbout;

        public SettingsFragment() {
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
                    FragmentActivity activity = getActivity();
                    if (activity != null) {
                        activity.getSupportFragmentManager().beginTransaction().addToBackStack(NAME);
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.settings, new SettingsNotificationFragment());
                    } else {
                        Toast.makeText(getContext(), "activity null", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.settings_home_general:
                    // general
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
        private static final String NAME = "SettingsAboutFragment";

        public SettingsAboutFragment() {
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
        private static final String NAME = "SettingsNotificationFragment";

        public SettingsNotificationFragment() {
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
}