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
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.TransactionBaseFragment;
import com.example.schoolairdroprefactoredition.scene.credit.CreditActivity;
import com.example.schoolairdroprefactoredition.scene.user.UserActivity;
import com.example.schoolairdroprefactoredition.scene.user.UserModifyInfoActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.HeaderOnlyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.UserHomeBaseInfo;
import com.example.schoolairdroprefactoredition.ui.components.UserHomeTransactionInfo;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.lxj.xpopup.XPopup;

public class UserFragment extends TransactionBaseFragment implements UserHomeBaseInfo.OnBaseInfoActionListener, UserHomeTransactionInfo.OnMoreInfoActionListener, UserActivity.OnUserInfoUpdatedListener {

    private UserViewModel viewModel;

    private String userInfoModify;
    private String userCredits;
    private String userTransactionsHistory;

    private RecyclerView mRecycler;
    private UserHomeBaseInfo mBaseInfo;
    private UserHomeTransactionInfo mTransactionInfo;

    private FragmentManager manager;

    private Bundle bundle;

    private DomainUserInfo.DataBean info;
    private DomainAuthorize token;

    public static UserFragment newInstance(Bundle bundle) {
        UserFragment fragment = new UserFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        userInfoModify = getResources().getString(R.string.modifyInfo);
        userCredits = getResources().getString(R.string.credits);
        userTransactionsHistory = getResources().getString(R.string.transactionsHistory);

        if (getActivity() != null) manager = getActivity().getSupportFragmentManager();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof UserActivity) {
            ((UserActivity) getActivity()).setOnUserInfoUpdateListener(this);
        }

        bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        } else {
            info = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
            token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentUserBinding binding = FragmentUserBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setHasOptionsMenu(true);

        mRecycler = binding.userRecycler;
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mBaseInfo = new UserHomeBaseInfo(getContext());
        mTransactionInfo = new UserHomeTransactionInfo(getContext());

        mBaseInfo.setOnBaseInfoActionListener(this);
        mTransactionInfo.setOnMoreInfoActionListener(this);

        HeaderOnlyRecyclerAdapter adapter = new HeaderOnlyRecyclerAdapter();
        adapter.addHeaderView(mBaseInfo);
        adapter.addHeaderView(mTransactionInfo);

        mRecycler.setAdapter(adapter);

        setUserInfo();

        return binding.getRoot();
    }

    /**
     * 使用用户信息装填ui界面
     * 在用户第一次进入页面时以及用户修改信息后返回时调用
     */
    private void setUserInfo() {
        mBaseInfo.setUserBaseInfo(info);
        mTransactionInfo.setUserMoreInfo(info);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.modify_info) {
            UserModifyInfoActivity.startForResult(getContext(), bundle);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.user_personal, menu);
    }

    /**
     * {@link UserActivity} 收到用户信息修改的回调
     *
     * @param info
     */
    @Override
    public void onUpdated(DomainUserInfo.DataBean info) {
        bundle.putSerializable(ConstantUtil.KEY_USER_INFO, info);
        this.info = info;
        setUserInfo();
    }

    @Override
    public void onAvatarClick(ImageView src) {
        new XPopup.Builder(getContext())
                .asImageViewer(src, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + info.getUser_img_path(), false, -1, -1, 50, true, new MyUtil.ImageLoader())
                .show();
    }

    @Override
    public void onTransactionClick() {
        // 交易记录
    }

    @Override
    public void onCreditsClick() {
        // 信用界面
        CreditActivity.start(getContext(), bundle);
    }
}
