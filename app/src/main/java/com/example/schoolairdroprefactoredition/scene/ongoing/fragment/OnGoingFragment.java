package com.example.schoolairdroprefactoredition.scene.ongoing.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.databinding.FragmentRecyclerBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainOnGoing;
import com.example.schoolairdroprefactoredition.scene.base.StatePlaceholderFragment;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.ui.adapter.OnGoingRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;

import java.util.List;

public class OnGoingFragment extends StatePlaceholderFragment implements BaseStateViewModel.OnRequestListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int PAGE_SENT = 0;
    private static final int PAGE_RECEIVED = 1;

    private FragmentRecyclerBinding binding;

    private OnGoingViewModel viewModel;

    private int index;

    private OnGoingRecyclerAdapter mAdapter;

    public static OnGoingFragment newInstance(int index) {
        OnGoingFragment fragment = new OnGoingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecyclerBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(OnGoingViewModel.class);
        viewModel.setOnRequestListener(this);

        index = PAGE_SENT;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        mAdapter = new OnGoingRecyclerAdapter();
        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.recycler.addItemDecoration(new MarginItemDecoration(SizeUtils.dp2px(8), false));
        binding.recycler.setAdapter(mAdapter);

        getOnGoing();

        return binding.getRoot();
    }

    public void getOnGoing() {
        DomainAuthorize token = null;
        try {
            token = (DomainAuthorize) getActivity().getIntent().getSerializableExtra(ConstantUtil.KEY_AUTHORIZE);
        } catch (NullPointerException ignored) {
        }

        if (token == null) {
            showPlaceholder(StatePlaceHolder.TYPE_EMPTY);
            return;
        }

        if (index == PAGE_SENT) {
            showPlaceholder(StatePlaceHolder.TYPE_LOADING);
            viewModel.getSent(token.getAccess_token()).observe(getViewLifecycleOwner(), sent -> {
                if (sent.size() == 0) {
                    showPlaceholder(StatePlaceHolder.TYPE_EMPTY);
                } else {
                    mAdapter.setList(sent);
                    showContentContainer();
                }
            });
        } else {
            showPlaceholder(StatePlaceHolder.TYPE_LOADING);
            viewModel.getReceived(token.getAccess_token()).observe(getViewLifecycleOwner(), received -> {
                if (received.size() == 0) {
                    showPlaceholder(StatePlaceHolder.TYPE_EMPTY);
                } else {
                    mAdapter.setList(received);
                    showContentContainer();
                }
            });
        }
    }

    private void sortData(List<DomainOnGoing.Data> data) {

    }

    @Override
    public void setContainerAndPlaceholder() {
        mStatePlaceholderFragmentContainer = binding.recycler;
        mStatePlaceholderFragmentPlaceholder = binding.placeholder;
    }

    @Override
    public void onError() {
        dismissLoading(() -> showPlaceholder(StatePlaceHolder.TYPE_ERROR));
    }
}
