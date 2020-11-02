package com.example.schoolairdroprefactoredition.scene.main.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentMyBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.scene.ongoing.OnGoingActivity;
import com.example.schoolairdroprefactoredition.scene.quote.QuoteActivity;
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity;
import com.example.schoolairdroprefactoredition.scene.settings.SettingsActivity;
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.scene.trash.TrashBinActivity;
import com.example.schoolairdroprefactoredition.scene.user.UserActivity;
import com.example.schoolairdroprefactoredition.ui.components.SSBInfo;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.ImageUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class MyFragment extends Fragment implements View.OnClickListener, MainActivity.OnLoginStateChangedListener, BaseStateViewModel.OnRequestListener, SSBInfo.OnSSBActionListener {

    private MyViewModel viewModel;

    private ImageView mAvatar;
    private TextView mName;

    private LoadingPopupView mLoading;

    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setOnLoginActivityListener(this);
            ((MainActivity) getActivity()).autoLogin();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMyBinding binding = FragmentMyBinding.inflate(LayoutInflater.from(getContext()));
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        viewModel.setOnRequestListener(this);

        mAvatar = binding.myAvatar;
        mName = binding.myName;
        mLoading = MyUtil.loading(getContext());

        binding.myInfo.setOnClickListener(this);
        binding.myQuote.setOnClickListener(this);
        binding.myOnGoing.setOnClickListener(this);
        binding.myTrash.setOnClickListener(this);
        binding.myLikes.setOnClickListener(this);
        binding.mySettings.setOnClickListener(this);
        binding.rootLayout.setOnRefreshListener(RefreshLayout::finishRefresh);
        binding.myBoughtSold.setOnSSBActionListener(this);

        setUserData();

        return binding.getRoot();
    }

    private Intent getIntent() {
        if (getActivity() instanceof MainActivity) {
            return getActivity().getIntent();
        }
        return new Intent();
    }

    private DomainUserInfo.DataBean getInfo() {
        return (DomainUserInfo.DataBean) getIntent().getSerializableExtra(ConstantUtil.KEY_USER_INFO);
    }

    private DomainAuthorize getToken() {
        return (DomainAuthorize) getIntent().getSerializableExtra(ConstantUtil.KEY_AUTHORIZE);
    }

    /**
     * 使用当前页面保存的bundle来显示用户信息
     * 若用户token与info都存在 则使用服务器数据填充页面
     * 否则使用页面默认值填充
     */
    private void setUserData() {
        DomainUserInfo.DataBean info = getInfo();
        if (getToken() == null) { // 无token认证信息，显示默认值
            mAvatar.setImageResource(R.drawable.ic_logo_alpha);
            mName.setText(getString(R.string.pleaseLogin));
        } else if (info != null) { // 设置页面数据
            ImageUtil.loadRoundedImage(mAvatar, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + info.getUser_img_path());
            mName.setText(info.getUname());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = getIntent();
        switch (id) {
            case R.id.my_info:
                if (intent != null && intent.getSerializableExtra(ConstantUtil.KEY_AUTHORIZE) != null
                        && intent.getSerializableExtra(ConstantUtil.KEY_USER_INFO) != null) {
                    UserActivity.start(getActivity(), true,
                            (DomainAuthorize) intent.getSerializableExtra(ConstantUtil.KEY_AUTHORIZE),
                            intent.getSerializableExtra(ConstantUtil.KEY_USER_INFO));
                } else {
                    LoginActivity.startForLogin(getContext());
                }
                break;
            case R.id.my_onGoing:
                OnGoingActivity.start(getContext(), intent.getExtras());
                break;
            case R.id.my_likes:
                // list my likes
                break;
            case R.id.my_trash:
                // list trash
                TrashBinActivity.Companion.start(getContext());

                break;
            case R.id.my_settings:
                if (getToken() == null)
                    SettingsActivity.startForResultLogin(getContext(), intent.getExtras());
                else
                    SettingsActivity.startForResultLogout(getContext(), intent.getExtras());
                break;
            case R.id.my_quote:
                QuoteActivity.start(getContext(), intent.getExtras());
                break;
        }
    }

    /**
     * 来自{@link MainActivity}的
     * 登录状态改变的回调
     */
    @Override
    public void onLoginStateChanged() {
        DomainAuthorize token = getToken();
        setUserData();
        if (token != null)
            viewModel.getUserInfo(token.getAccess_token()).observe(getViewLifecycleOwner(), data -> {
                setUserData();
            });
    }

    /**
     * 在售、已售、已购三个点击事件回调
     * {@link SSBInfo.OnSSBActionListener}
     */
    @Override
    public void onSellingClick(View view) {
        SSBActivity.start(getContext(), getToken(), getInfo(), 0, true);
    }

    @Override
    public void onSoldClick(View view) {
        SSBActivity.start(getContext(), getToken(), getInfo(), 1, true);
    }

    @Override
    public void onBoughtClick(View view) {
        SSBActivity.start(getContext(), getToken(), getInfo(), 2, true);
    }

    @Override
    public void onError() {
        Toast.makeText(getContext(), "Error getting user info", Toast.LENGTH_SHORT).show();
    }

}