package com.example.schoolairdroprefactoredition.fragment.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.SettingsActivity;
import com.example.schoolairdroprefactoredition.activity.UserActivity;
import com.example.schoolairdroprefactoredition.databinding.FragmentMyBinding;
import com.example.schoolairdroprefactoredition.ui.components.BoughtSoldInfo;

public class MyFragment extends Fragment implements View.OnClickListener {

    private ImageView mAvatar;
    private TextView mName;
    private BoughtSoldInfo mBoughtSoldInfo;
    private Bundle mUserBundle;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        FragmentMyBinding binding = FragmentMyBinding.inflate(inflater, container, false);

        mUserBundle = new Bundle();
        mAvatar = binding.myAvatar;
        mName = binding.myName;
        mBoughtSoldInfo = binding.myBoughtSold;

        binding.myInfo.setOnClickListener(this);
        binding.myOnGoing.setOnClickListener(this);
        binding.myLikes.setOnClickListener(this);
        binding.mySettings.setOnClickListener(this);

//        myViewModel.getAvatar()
        myViewModel.getUserInfo().observe(getViewLifecycleOwner(), data -> {
            mName.setText(data.getName());
            mBoughtSoldInfo.setSelling(data.getSelling());
            mBoughtSoldInfo.setSold(data.getSold());
            mBoughtSoldInfo.setBought(data.getBought());
        });

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.my_info:
                Intent intentInfo = new Intent(getContext(), UserActivity.class);
                getContext().startActivity(intentInfo);
                break;
            case R.id.my_onGoing:
                // list on going
                break;
            case R.id.my_likes:
                // list my likes
                break;
            case R.id.my_settings:
                // open settings
                Intent intentSetting = new Intent(getContext(), SettingsActivity.class);
                getContext().startActivity(intentSetting);
                break;
        }
    }
}