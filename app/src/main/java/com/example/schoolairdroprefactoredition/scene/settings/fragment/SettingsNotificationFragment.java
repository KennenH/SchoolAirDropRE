package com.example.schoolairdroprefactoredition.scene.settings.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.base.TransitionBaseFragment;
import com.example.schoolairdroprefactoredition.ui.components.PageItem;

/**
 * 通知页面
 */
public class SettingsNotificationFragment extends TransitionBaseFragment implements View.OnClickListener {

    public SettingsNotificationFragment() {
    }

    public static SettingsNotificationFragment newInstance(Bundle bundle) {
        SettingsNotificationFragment fragment = new SettingsNotificationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private PageItem mReceiveNew;
    private PageItem mReceiveInApp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_notification, container, false);

        mReceiveNew = root.findViewById(R.id.settings_notification_receive);
        mReceiveInApp = root.findViewById(R.id.settings_notification_in_app);

        mReceiveNew.setOnClickListener(this);
        mReceiveInApp.setOnClickListener(this);

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
        }
    }
}