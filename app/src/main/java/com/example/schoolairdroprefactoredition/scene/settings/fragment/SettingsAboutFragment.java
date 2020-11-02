package com.example.schoolairdroprefactoredition.scene.settings.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentSettingsAboutBinding;
import com.example.schoolairdroprefactoredition.scene.base.TransitionBaseFragment;
import com.example.schoolairdroprefactoredition.ui.components.PageItem;

/**
 * 关于页面
 */
public class SettingsAboutFragment extends TransitionBaseFragment implements View.OnClickListener {

    private TextView mVersion;
    private PageItem mWhatsNew;
    private PageItem mFeedback;

    public SettingsAboutFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentSettingsAboutBinding binding = FragmentSettingsAboutBinding.inflate(inflater, container, false);

        mVersion = binding.settingsAboutVersion;
        mWhatsNew = binding.settingsAboutNew;
        mFeedback = binding.settingsAboutFeedback;

        mVersion.setOnClickListener(this);
        mWhatsNew.setOnClickListener(this);
        mFeedback.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.settings_about_version) {

        } else if (id == R.id.settings_about_new) {
            // 同一版本号,制定更新增加版本号的规则
        } else if (id == R.id.settings_about_feedback) {
            // 发送邮箱或app内文字加图片形式
        }
    }
}