package com.example.schoolairdroprefactoredition.scene.main.my;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class MyFragment extends Fragment implements View.OnClickListener, MainActivity.OnLoginActivityListener, BaseStateViewModel.OnRequestListener, SSBInfo.OnSSBActionListener {

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();

        info = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
        token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);

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
        binding.myTrash.setOnClickListener(this);
        binding.myLikes.setOnClickListener(this);
        binding.mySettings.setOnClickListener(this);
        binding.rootLayout.setOnRefreshListener(RefreshLayout::finishRefresh);
        mSSBInfo.setOnSSBActionListener(this);

        return binding.getRoot();
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
                SettingsActivity.startForResult(getContext(), bundle);
                break;
            case R.id.my_quote:
                QuoteActivity.start(getContext(), bundle);
                break;
        }
    }

    /**
     * {@link MainActivity}收到来自{@link SettingsActivity}返回的登录成功信息 或者 自身收到用户信息修改 后的回调
     * #if 若bundle中的用户信息不为空，则直接使用该信息填充页面
     * #else 若bundle中token信息不为空，则使用token换取用户信息并填充页面
     */
    @Override
    public void onLoginActivity(Bundle bundle) {
        if (bundle != null) {
            this.bundle = bundle;

            DomainUserInfo.DataBean info = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
            if (info != null) {
                mAvatar.setImageURI(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + info.getUser_img_path());
                mName.setText(info.getUname());
            } else {
                DomainAuthorize authorize = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
                if (authorize != null)
                    viewModel.getUserInfo(authorize.getAccess_token()).observe(getViewLifecycleOwner(), data -> {
                        DomainUserInfo.DataBean info_ = data.getData().get(0);
                        mAvatar.setImageURI(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + info_.getUser_img_path());
                        mName.setText(info_.getUname());
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
}