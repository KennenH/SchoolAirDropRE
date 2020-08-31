package com.example.schoolairdroprefactoredition.fragment.my;

import android.os.Bundle;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.MainActivity;
import com.example.schoolairdroprefactoredition.activity.ongoing.OnGoingActivity;
import com.example.schoolairdroprefactoredition.activity.quote.QuoteActivity;
import com.example.schoolairdroprefactoredition.activity.settings.SettingsActivity;
import com.example.schoolairdroprefactoredition.activity.settings.UserActivity;
import com.example.schoolairdroprefactoredition.databinding.FragmentMyBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainGetUserInfo;
import com.example.schoolairdroprefactoredition.fragment.home.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.ui.components.SSBInfo;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

public class MyFragment extends Fragment implements View.OnClickListener, MainActivity.OnLoginActivityListener, BaseChildFragmentViewModel.OnRequestListener {

    private MyViewModel viewModel;

    private SimpleDraweeView mAvatar;
    private TextView mName;
    private SSBInfo mSSBInfo;

    private LoadingPopupView mLoading;

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
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setOnLoginActivityListener(this);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMyBinding binding = FragmentMyBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        viewModel.setOnRequestListener(this);

        mAvatar = binding.myAvatar;
        mName = binding.myName;
        mSSBInfo = binding.myBoughtSold;
        mLoading = new XPopup.Builder(getContext()).asLoading();

        binding.myInfo.setOnClickListener(this);
        binding.myQuote.setOnClickListener(this);
        binding.myOnGoing.setOnClickListener(this);
        binding.myLikes.setOnClickListener(this);
        binding.mySettings.setOnClickListener(this);
        binding.rootLayout.setOnRefreshListener(RefreshLayout::finishRefresh);

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
                SettingsActivity.startForResult(getContext(), bundle);
                break;
            case R.id.my_quote:
                QuoteActivity.start(getContext(), bundle);
                break;
        }
    }

    /**
     * {@link MainActivity}收到来自{@link SettingsActivity}返回的登录成功信息后的回调
     */
    @Override
    public void onLoginActivity(Bundle bundle) {
        if (bundle != null) {
            this.bundle = bundle;

            DomainGetUserInfo info = (DomainGetUserInfo) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
            if (info != null) {
                mAvatar.setImageURI(info.getUser_info().getUser_img_path());
                mName.setText(info.getUser_info().getUname());
            } else {
                DomainAuthorize authorize = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
                if (authorize != null)
                    viewModel.getUserInfo(authorize.getAccess_token()).observe(getViewLifecycleOwner(), data -> {
                        mAvatar.setImageURI(data.getUser_info().getUser_img_path());
                        mName.setText(data.getUser_info().getUname());
                    });
                else
                    Toast.makeText(getContext(), "error login", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onError() {
        Toast.makeText(getContext(), "error getting user info", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading() {

    }
}