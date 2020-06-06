package com.example.schoolairdroprefactoredition.ui.my;

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
import com.example.schoolairdroprefactoredition.ui.components.PageItem;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

public class MyFragment extends Fragment implements View.OnClickListener {
    private ImageView mAvatar;
    private TextView mName;
    private TextView mSelling;
    private TextView mSold;
    private TextView mPurchased;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my, container, false);

        mAvatar = root.findViewById(R.id.my_avatar);
        mName = root.findViewById(R.id.my_name);
        mSelling = root.findViewById(R.id.my_selling);
        mSold = root.findViewById(R.id.my_sold);
        mPurchased = root.findViewById(R.id.my_bought);

        root.findViewById(R.id.my_info).setOnClickListener(this);
        root.findViewById(R.id.my_selling_wrapper).setOnClickListener(this);
        root.findViewById(R.id.my_sold_wrapper).setOnClickListener(this);
        root.findViewById(R.id.my_bought_wrapper).setOnClickListener(this);
        root.findViewById(R.id.my_onGoing).setOnClickListener(this);
        root.findViewById(R.id.my_likes).setOnClickListener(this);
        root.findViewById(R.id.my_settings).setOnClickListener(this);

//        myViewModel.getAvatar()
        myViewModel.getName().observe(getViewLifecycleOwner(), name -> mName.setText(name));
        myViewModel.getSelling().observe(getViewLifecycleOwner(), name -> mSelling.setText(name));
        myViewModel.getSold().observe(getViewLifecycleOwner(), name -> mSold.setText(name));
        myViewModel.getPurchased().observe(getViewLifecycleOwner(), name -> mPurchased.setText(name));

        return root;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.my_info:
                Intent intentInfo = new Intent(getContext(), UserActivity.class);
                getContext().startActivity(intentInfo);
                break;
            case R.id.my_selling_wrapper:
                // my selling
                break;
            case R.id.my_sold_wrapper:
                // history sold
                break;
            case R.id.my_bought_wrapper:
                // history bought
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