package com.example.schoolairdroprefactoredition.scene.user.user;

import android.content.Context;
import android.content.Intent;
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
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainGetUserInfo;
import com.example.schoolairdroprefactoredition.scene.credit.CreditActivity;
import com.example.schoolairdroprefactoredition.databinding.FragmentUserBinding;
import com.example.schoolairdroprefactoredition.scene.base.TransactionBaseFragment;
import com.example.schoolairdroprefactoredition.ui.adapter.HeaderOnlyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.UserHomeBaseInfo;
import com.example.schoolairdroprefactoredition.ui.components.UserHomeMoreInfo;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.lxj.xpopup.XPopup;

public class UserFragment extends TransactionBaseFragment implements UserHomeBaseInfo.OnBaseInfoActionListener, UserHomeMoreInfo.OnMoreInfoActionListener {

    private UserViewModel viewModel;

    private String userInfoModify;
    private String userCredits;
    private String userTransactionsHistory;

    private RecyclerView mRecycler;
    private UserHomeBaseInfo mBaseInfo;
    private UserHomeMoreInfo mMoreInfo;

    private FragmentManager manager;

    private Bundle bundle;

    private DomainGetUserInfo.DataBean info;
    private DomainAuthorize token;

    public static UserFragment newInstance(Bundle bundle) {
        UserFragment fragment = new UserFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static UserFragment newInstance(Intent intent) {
        UserFragment fragment = new UserFragment();
        fragment.setArguments(intent.getExtras());
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
        bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        } else {
            info = (DomainGetUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
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
        mMoreInfo = new UserHomeMoreInfo(getContext());

        mBaseInfo.setOnBaseInfoActionListener(this);
        mMoreInfo.setOnMoreInfoActionListener(this);

        HeaderOnlyRecyclerAdapter adapter = new HeaderOnlyRecyclerAdapter();
        adapter.addHeaderView(mBaseInfo);
        adapter.addHeaderView(mMoreInfo);

        mRecycler.setAdapter(adapter);

        init();

        return binding.getRoot();
    }

    private void init() {
        mBaseInfo.setUserBaseInfo(info);
        mMoreInfo.setUserMoreInfo(info);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.modify_info) {
            transact(manager, UserModifyInfoFragment.newInstance(bundle), userInfoModify);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.user_personal, menu);
    }

    @Override
    public void onAvatarClick(ImageView src) {
        new XPopup.Builder(getContext())
                .asImageViewer(src, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + info.getUser_img_path(), false, -1, -1, 50, false, new MyUtil.ImageLoader())
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
