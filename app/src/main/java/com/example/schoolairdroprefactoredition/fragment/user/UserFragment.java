package com.example.schoolairdroprefactoredition.fragment.user;

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
import com.example.schoolairdroprefactoredition.activity.credit.CreditActivity;
import com.example.schoolairdroprefactoredition.databinding.FragmentUserBinding;
import com.example.schoolairdroprefactoredition.fragment.TransactionBaseFragment;
import com.example.schoolairdroprefactoredition.ui.adapter.HeaderOnlyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.UserHomeBaseInfo;
import com.example.schoolairdroprefactoredition.ui.components.UserHomeMoreInfo;
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        userInfoModify = getResources().getString(R.string.modifyInfo);
        userCredits = getResources().getString(R.string.credits);
        userTransactionsHistory = getResources().getString(R.string.transactionsHistory);

        if (getActivity() != null) manager = getActivity().getSupportFragmentManager();
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

        return binding.getRoot();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.modify_info) {
            transact(manager, new UserModifyInfoFragment(), userInfoModify);
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
                .asImageViewer(src, R.drawable.logo, false, -1, -1, 50, false, new MyUtil.ImageLoader())
                .show();
    }

    @Override
    public void onTransactionClick() {
        // 交易记录
    }

    @Override
    public void onCreditsClick() {
        // 信用界面
        CreditActivity.start(getContext());
    }
}
