package com.example.schoolairdroprefactoredition.scene.quote.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolairdroprefactoredition.databinding.FragmentRecyclerBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.base.StatePlaceholderFragment;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.ui.adapter.QuoteRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.decoration.QuoteDecoration;

import java.util.ArrayList;
import java.util.List;

public class QuoteFragment extends StatePlaceholderFragment implements BaseStateViewModel.OnRequestListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int PAGE_RECEIVED = 0;
    private static final int PAGE_SENT = 1;

    private QuoteViewModel viewModel;

    private int index;

    private FragmentRecyclerBinding binding;

    private QuoteRecyclerAdapter mAdapter;

    public static QuoteFragment newInstance(int index) {
        QuoteFragment fragment = new QuoteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecyclerBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(QuoteViewModel.class);
        viewModel.setOnRequestListener(this);

        index = PAGE_RECEIVED;
        if (getArguments() != null)
            index = getArguments().getInt(ARG_SECTION_NUMBER);

        if (getActivity() != null)
            mAdapter = new QuoteRecyclerAdapter(getActivity().getIntent().getExtras());
        else
            mAdapter = new QuoteRecyclerAdapter(new Bundle());

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.recycler.setLayoutManager(manager);

        showPlaceholder(StatePlaceHolder.TYPE_LOADING);
        getQuote();

        return binding.getRoot();
    }

    /**
     * 获取报价数据
     */
    private void getQuote() {
        Bundle bundle = null;
        DomainAuthorize token;

        if (getActivity() != null)
            bundle = getActivity().getIntent().getExtras();

        if (bundle != null)
            token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
        else {
            showPlaceholder(StatePlaceHolder.TYPE_ERROR);
            return;
        }

        if (token != null) {
            if (index == PAGE_RECEIVED) {
                viewModel.getQuoteReceived(token.getAccess_token()).observe(getViewLifecycleOwner(), received -> {
                    // 将排序后的报价列表分成两类
                    mAdapter.setList(sortData(received));// 此处还未排序,排序使用sortData方法
                    binding.recycler.addItemDecoration(new QuoteDecoration(getContext(), received.size(), 2));
                    binding.recycler.setAdapter(mAdapter);
                });
            } else {
                viewModel.getQuoteSent(token.getAccess_token()).observe(getViewLifecycleOwner(), sent -> {
                    // 同上
                    mAdapter.setList(sortData(sent));
                    binding.recycler.addItemDecoration(new QuoteDecoration(getContext(), sent.size(), 5));
                    binding.recycler.setAdapter(mAdapter);
                });
            }
            showContentContainer();
        } else showPlaceholder(StatePlaceHolder.TYPE_ERROR);
    }

    private List<DomainGoodsInfo.DataBean> sortData(List<DomainGoodsInfo.DataBean> data) {
        return new ArrayList<>();
    }

    @Override
    public void onError() {
        showPlaceholder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void setContainerAndPlaceholder() {
        mStatePlaceholderFragmentContainer = binding.recycler;
        mStatePlaceholderFragmentPlaceholder = binding.placeholder;
    }
}
