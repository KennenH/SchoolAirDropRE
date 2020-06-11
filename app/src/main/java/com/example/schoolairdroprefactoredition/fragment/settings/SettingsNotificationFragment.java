package com.example.schoolairdroprefactoredition.fragment.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.components.PageItem;

/**
 * 通知页面
 */
public class SettingsNotificationFragment extends Fragment implements View.OnClickListener {

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