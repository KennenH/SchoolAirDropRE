package com.example.schoolairdroprefactoredition.fragment.my;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.ongoing.OnGoingActivity;
import com.example.schoolairdroprefactoredition.activity.quote.QuoteActivity;
import com.example.schoolairdroprefactoredition.activity.settings.SettingsActivity;
import com.example.schoolairdroprefactoredition.activity.settings.UserActivity;
import com.example.schoolairdroprefactoredition.databinding.FragmentMyBinding;
import com.example.schoolairdroprefactoredition.ui.components.SSBInfo;

public class MyFragment extends Fragment implements View.OnClickListener {

    private ImageView mAvatar;
    private TextView mName;
    private SSBInfo mSSBInfo;

    private Bundle bundle;

    public static MyFragment newInstance(Bundle bundle) {
        MyFragment fragment = new MyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        FragmentMyBinding binding = FragmentMyBinding.inflate(inflater, container, false);

        mAvatar = binding.myAvatar;
        mName = binding.myName;
        mSSBInfo = binding.myBoughtSold;

        binding.myInfo.setOnClickListener(this);
        binding.myQuote.setOnClickListener(this);
        binding.myOnGoing.setOnClickListener(this);
        binding.myLikes.setOnClickListener(this);
        binding.mySettings.setOnClickListener(this);

//        myViewModel.getAvatar()
        myViewModel.getUserInfo().observe(getViewLifecycleOwner(), data -> {
            mName.setText(data.getName());
            mSSBInfo.setSelling(data.getSelling());
            mSSBInfo.setSold(data.getSold());
            mSSBInfo.setBought(data.getBought());
        });

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.my_info:
                UserActivity.start(getContext(), bundle);
                break;
            case R.id.my_onGoing:
                OnGoingActivity.start(getContext(), bundle);
                break;
            case R.id.my_likes:
                // list my likes
                break;
            case R.id.my_trash:
                // list trash
                break;
            case R.id.my_settings:
                SettingsActivity.startForResult(getContext());
                break;
            case R.id.my_quote:
                QuoteActivity.start(getContext(), bundle);
                break;
        }
    }
}