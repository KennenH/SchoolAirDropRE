package com.example.schoolairdroprefactoredition.scene.main.my;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.schoolairdroprefactoredition.scene.user.UserActivity;
import com.example.schoolairdroprefactoredition.ui.components.SSBInfo;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import org.jetbrains.annotations.NotNull;

public class MyFragment extends Fragment implements View.OnClickListener, MainActivity.OnLoginStateChangedListener, BaseStateViewModel.OnRequestListener, SSBInfo.OnSSBActionListener {

    private MyViewModel viewModel;

    private SimpleDraweeView mAvatar;
    private TextView mName;
    private SSBInfo mSSBInfo;

    private LoadingPopupView mLoading;

    private Bundle bundle = new Bundle();

    private DomainUserInfo.DataBean info;
    private DomainAuthorize token;

    public static MyFragment newInstance(Bundle bundle) {
        MyFragment fragment = new MyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();

        info = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
        token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setOnLoginActivityListener(this);
            ((MainActivity) getActivity()).autoLogin();
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
        binding.myTrash.setOnClickListener(this);
        binding.myLikes.setOnClickListener(this);
        binding.mySettings.setOnClickListener(this);
        binding.rootLayout.setOnRefreshListener(RefreshLayout::finishRefresh);
        mSSBInfo.setOnSSBActionListener(this);

        setUserData();

        return binding.getRoot();
    }

    /**
     * 使用当前页面保存的bundle来显示用户信息
     * 若用户token与info都存在 则使用服务器数据填充页面
     * 否则使用页面默认值填充
     */
    private void setUserData() {
        if (token == null) { // 无token认证信息，显示默认值
            mAvatar.setActualImageResource(R.drawable.logo);
            mName.setText(getString(R.string.pleaseLogin));
            mSSBInfo.setSelling(0);
            mSSBInfo.setBought(0);
            mSSBInfo.setSold(0);
        } else if (info != null) { // 设置页面数据
            mAvatar.setImageURI(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + info.getUser_img_path());
            mName.setText(info.getUname());
            // set selling sold bought number
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.my_info:
                if (bundle != null && bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE) != null
                        && bundle.getSerializable(ConstantUtil.KEY_USER_INFO) != null) {
                    UserActivity.startForResult(getActivity(), bundle);
                } else {
                    LoginActivity.startForLogin(getContext());
                }
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
                if (token == null)
                    SettingsActivity.startForResultLogin(getContext(), bundle);
                else
                    SettingsActivity.startForResultLogout(getContext(), bundle);
                break;
            case R.id.my_quote:
                QuoteActivity.start(getContext(), bundle);
                break;
        }
    }

    /**
     * 来自{@link MainActivity}的
     * 登录状态改变的回调
     */
    @Override
    public void onLoginStateChanged(@NotNull Bundle bundle) {
        this.bundle = bundle;

        this.info = (DomainUserInfo.DataBean) this.bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
        this.token = (DomainAuthorize) this.bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);

        setUserData();
        if (token != null)
            viewModel.getUserInfo(token.getAccess_token()).observe(getViewLifecycleOwner(), data -> {
                this.info = data.getData().get(0);
                setUserData();
            });
    }

    /**
     * 在售、已售、已购三个点击事件回调
     * {@link SSBInfo.OnSSBActionListener}
     */
    @Override
    public void onSellingClick(View view) {
        SSBActivity.start(getContext(), bundle, 0);
    }

    @Override
    public void onSoldClick(View view) {
        SSBActivity.start(getContext(), bundle, 1);
    }

    @Override
    public void onBoughtClick(View view) {
        SSBActivity.start(getContext(), bundle, 2);
    }

    @Override
    public void onError() {
        Toast.makeText(getContext(), "Error getting user info", Toast.LENGTH_SHORT).show();
    }

}