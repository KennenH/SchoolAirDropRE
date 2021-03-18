package com.example.schoolairdroprefactoredition.scene.settings.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.base.TransitionBaseFragment;

// 隐私
public class SettingsPrivacyFragment extends TransitionBaseFragment implements View.OnClickListener {

    public SettingsPrivacyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_privacy, container, false);

        root.findViewById(R.id.settings_privacy_blacklist).setOnClickListener(this);
        init();

        return root;
    }

    private void init() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.settings_privacy_blacklist) {
            // open blacklist
        }
    }
}
