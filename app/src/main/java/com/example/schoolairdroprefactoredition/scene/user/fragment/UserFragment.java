package com.example.schoolairdroprefactoredition.scene.user.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentUserBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.base.DomainBaseUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.TransactionBaseFragment;
import com.example.schoolairdroprefactoredition.scene.credit.CreditActivity;
import com.example.schoolairdroprefactoredition.scene.main.my.MyViewModel;
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.scene.user.UserActivity;
import com.example.schoolairdroprefactoredition.scene.user.UserModifyInfoActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.HeaderOnlyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.UserHomeBaseInfo;
import com.example.schoolairdroprefactoredition.ui.components.UserHomeSellingInfo;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.lxj.xpopup.XPopup;

import java.lang.reflect.InvocationTargetException;

import javadz.beanutils.BeanUtils;

public class UserFragment extends TransactionBaseFragment implements UserHomeBaseInfo.OnBaseInfoActionListener, UserHomeSellingInfo.OnMoreInfoActionListener, UserActivity.OnUserInfoUpdatedListener {

    private MyViewModel viewModel;

    private RecyclerView mRecycler;
    private UserHomeBaseInfo mBaseInfo;
    private UserHomeSellingInfo mSellingInfo;

    private FragmentManager manager;

    private Bundle bundle;

    private boolean isModifiable = false;
    private boolean isMine = false;
    private DomainAuthorize token;
    private DomainBaseUserInfo userInfo;

    public static UserFragment newInstance(Bundle bundle) {
        UserFragment fragment = new UserFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null) manager = getActivity().getSupportFragmentManager();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof UserActivity)
            ((UserActivity) getActivity()).setOnUserInfoUpdateListener(this);

        bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        } else {
            isModifiable = bundle.getBoolean(ConstantUtil.KEY_INFO_MODIFIABLE);
            token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
            userInfo = (DomainBaseUserInfo) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentUserBinding binding = FragmentUserBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        if (token != null)
            viewModel.getUserInfo(token.getAccess_token()).observe(getViewLifecycleOwner(), userInfo -> {
                if (userInfo.getData().get(0).getUid() == this.userInfo.getUid())
                    isMine = true;
            });

        if (isModifiable)
            setHasOptionsMenu(true);

        mRecycler = binding.userRecycler;
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mBaseInfo = new UserHomeBaseInfo(getContext());
        mSellingInfo = new UserHomeSellingInfo(getContext());

        mBaseInfo.setOnBaseInfoActionListener(this);
        mSellingInfo.setOnMoreInfoActionListener(this);

        HeaderOnlyRecyclerAdapter adapter = new HeaderOnlyRecyclerAdapter();
        adapter.addHeaderView(mBaseInfo);
        adapter.addHeaderView(mSellingInfo);

        mRecycler.setAdapter(adapter);

        setUserInfo();

        return binding.getRoot();
    }

    /**
     * 使用用户信息装填ui界面
     * 在用户第一次进入页面时以及用户修改信息后返回时调用
     */
    private void setUserInfo() {
        mBaseInfo.setUserBaseInfo(userInfo);
        mSellingInfo.setUserMoreInfo(userInfo);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.modify_info) {
            UserModifyInfoActivity.start(getContext(), token, userInfo);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.user_personal, menu);
    }

    /**
     * {@link UserActivity} 收到用户信息修改的回调
     */
    @Override
    public void onUpdated(Object info) {
        try {
            BeanUtils.copyProperties(userInfo, info);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        bundle.putSerializable(ConstantUtil.KEY_USER_INFO, userInfo);
        setUserInfo();
    }

    @Override
    public void onAvatarClick(ImageView src) {
        new XPopup.Builder(getContext())
                .asImageViewer(src, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + userInfo.getUser_img_path(), false, -1, -1, 50, true, new MyUtil.ImageLoader())
                .show();
    }

    @Override
    public void onSellingClick() {
        SSBActivity.start(getContext(), token, userInfo, 0, isModifiable || isMine);
    }

    @Override
    public void onCreditsClick() {
        if (isModifiable)
            CreditActivity.start(getContext(), bundle);
    }
}
