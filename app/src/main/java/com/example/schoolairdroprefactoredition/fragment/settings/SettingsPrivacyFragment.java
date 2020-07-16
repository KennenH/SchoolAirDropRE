package com.example.schoolairdroprefactoredition.fragment.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.fragment.TransactionBaseFragment;
import com.example.schoolairdroprefactoredition.ui.components.PageItem;

// 隐私
public class SettingsPrivacyFragment extends TransactionBaseFragment implements View.OnClickListener {

    private PageItem mAddViaAlipay;
    private PageItem mRecommendFriends;
    private PageItem mBlackList;

    public SettingsPrivacyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_privacy, container, false);

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
