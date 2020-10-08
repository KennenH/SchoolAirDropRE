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
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.TransactionBaseFragment;
import com.example.schoolairdroprefactoredition.scene.credit.CreditActivity;
import com.example.schoolairdroprefactoredition.scene.user.UserActivity;
import com.example.schoolairdroprefactoredition.scene.user.UserModifyInfoActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.HeaderOnlyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.UserHomeBaseInfo;
import com.example.schoolairdroprefactoredition.ui.components.UserHomeSellingInfo;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.lxj.xpopup.XPopup;

public class UserFragment extends TransactionBaseFragment implements UserHomeBaseInfo.OnBaseInfoActionListener, UserHomeSellingInfo.OnMoreInfoActionListener, UserActivity.OnUserInfoUpdatedListener {

    private UserViewModel viewModel;

    private RecyclerView mRecycler;
    private UserHomeBaseInfo mBaseInfo;
    private UserHomeSellingInfo mSellingInfo;

    private FragmentManager manager;

    private Bundle bundle;

    private DomainUserInfo.DataBean myInfo;
    private DomainAuthorize token;
    private DomainGoodsInfo.DataBean goodsInfo;
    private boolean isModifiable = false;

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
            myInfo = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
            token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
            goodsInfo = (DomainGoodsInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_GOODS_INFO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentUserBinding binding = FragmentUserBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // 若查看自己的个人信息页面且是从MyFragment中进入的则显示修改按钮，否则不显示
        if (isModifiable && isMine())
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
     * 是否查看的是自己的页面
     */
    private boolean isMine() {
        if (token != null) {
            if (myInfo != null)
                return goodsInfo == null ||
                        goodsInfo.getSeller_info() == null ||
                        goodsInfo.getSeller_info().getUid() == myInfo.getUid();
        }
        return false;
    }

    /**
     * 使用用户信息装填ui界面
     * 在用户第一次进入页面时以及用户修改信息后返回时调用
     */
    private void setUserInfo() {
        if (isMine()) {
            mBaseInfo.setUserBaseInfo(myInfo);
            mSellingInfo.setUserMoreInfo(myInfo);
        } else {
            mBaseInfo.setUserBaseInfo(goodsInfo);
            mSellingInfo.setUserMoreInfo(goodsInfo);
        }
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
     */
    @Override
    public void onUpdated(DomainUserInfo.DataBean info) {
        bundle.putSerializable(ConstantUtil.KEY_USER_INFO, info);
        this.myInfo = info;
        setUserInfo();
    }

    @Override
    public void onAvatarClick(ImageView src) {
        if (isMine())
            new XPopup.Builder(getContext())
                    .asImageViewer(src, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + myInfo.getUser_img_path(), false, -1, -1, 50, true, new MyUtil.ImageLoader())
                    .show();
        else if (goodsInfo != null && goodsInfo.getSeller_info() != null)
            new XPopup.Builder(getContext())
                    .asImageViewer(src, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + goodsInfo.getSeller_info().getUser_img_path(), false, -1, -1, 50, true, new MyUtil.ImageLoader())
                    .show();
    }

    @Override
    public void onSellingClick() {
//        SSBActivity.start(getContext(), bundle);
    }

    @Override
    public void onCreditsClick() {
        if (isMine())
            CreditActivity.start(getContext(), bundle);
    }
}
