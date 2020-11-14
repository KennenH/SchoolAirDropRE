package com.example.schoolairdroprefactoredition.scene.tab.quote.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentRecyclerBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainQuote;
import com.example.schoolairdroprefactoredition.scene.base.StatePlaceholderFragment;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.ui.adapter.QuoteRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DialogUtil;
import com.example.schoolairdroprefactoredition.utils.decoration.QuoteDecoration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuoteFragment extends StatePlaceholderFragment implements BaseStateViewModel.OnRequestListener, QuoteRecyclerAdapter.OnQuoteActionListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final int PAGE_RECEIVED = 0;
    public static final int PAGE_SENT = 1;

    private QuoteViewModel viewModel;

    private int index;

    private FragmentRecyclerBinding binding;

    private QuoteRecyclerAdapter mAdapter;

    private int unHandled;

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

        if (getActivity() != null) {
            Bundle bundle = getActivity().getIntent().getExtras();
            if (bundle != null)
                bundle.putInt(ConstantUtil.FRAGMENT_NUM, index);
            else
                bundle = new Bundle();
            mAdapter = new QuoteRecyclerAdapter(bundle);
        }

        mAdapter.setOnQuoteActionListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.recycler.setLayoutManager(manager);
        binding.recycler.setAdapter(mAdapter);

        getQuote();

        return binding.getRoot();
    }

    @Override
    public StatePlaceHolder getStatePlaceholder() {
        return binding.placeholder;
    }

    @Override
    public View getContentContainer() {
        return binding.recycler;
    }

    /**
     * 获取报价数据
     */
    private void getQuote() {
        unHandled = 0;
        DomainAuthorize token = null;
        try {
            token = (DomainAuthorize) getActivity().getIntent().getSerializableExtra(ConstantUtil.KEY_AUTHORIZE);
        } catch (NullPointerException ignored) {
        }

        if (token != null) {
            showPlaceholder(StatePlaceHolder.TYPE_LOADING);
            if (index == PAGE_RECEIVED) {
                viewModel.getQuoteReceived(token.getAccess_token()).observe(getViewLifecycleOwner(), received -> {
                    if (received.size() == 0) {
                        showPlaceholder(StatePlaceHolder.TYPE_EMPTY);
                    } else {
                        mAdapter.setList(sortDataByPrecess(received));
                        binding.recycler.addItemDecoration(new QuoteDecoration(getContext(), received.size(), unHandled));
                        showContentContainer();
                    }
                });
            } else {
                viewModel.getQuoteSent(token.getAccess_token()).observe(getViewLifecycleOwner(), sent -> {
                    if (sent.size() == 0) {
                        showPlaceholder(StatePlaceHolder.TYPE_EMPTY);
                    } else {
                        mAdapter.setList(sortDataByPrecess(sent));
                        binding.recycler.addItemDecoration(new QuoteDecoration(getContext(), sent.size(), unHandled));
                        showContentContainer();
                    }
                });
            }
        } else showPlaceholder(StatePlaceHolder.TYPE_EMPTY);
    }

    /**
     * 将未处理的item至于列表最前
     */
    private List<DomainQuote.DataBean> sortDataByPrecess(List<DomainQuote.DataBean> data) {
        List<DomainQuote.DataBean> unhandledList = new ArrayList<>();
        for (Iterator<DomainQuote.DataBean> iterator = data.iterator(); iterator.hasNext(); ) {
            DomainQuote.DataBean DataBean = iterator.next();
            if (DataBean.getState() == ConstantUtil.QUOTE_STATE_UNHANDLED) {
                unhandledList.add(DataBean);
                iterator.remove();
            }
        }
        data.addAll(0, unhandledList);
        unHandled = unhandledList.size();
        return data;
    }

    @Override
    public void onError() {
        dismissLoading(() -> showPlaceholder(StatePlaceHolder.TYPE_ERROR));
    }

    @Override
    public void onQuoteAccept(String quoteID) {
        DomainAuthorize token = null;
        try {
            token = (DomainAuthorize) getActivity().getIntent().getSerializableExtra(ConstantUtil.KEY_AUTHORIZE);
        } catch (NullPointerException ignored) {
        }

        if (token != null) {
            showLoading();
            viewModel.acceptQuote(token.getAccess_token(), quoteID).observe(getViewLifecycleOwner(), success -> {
                dismissLoading(() -> {
                    DialogUtil.showCenterDialog(getContext(), DialogUtil.DIALOG_TYPE.SUCCESS, R.string.accepted);
                    getQuote();
                });
            });
        } else {
            DialogUtil.showCenterDialog(getContext(), DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed);
        }
    }

    @Override
    public void onQuoteRefuse(String quoteID) {
        DomainAuthorize token = null;
        try {
            token = (DomainAuthorize) getActivity().getIntent().getSerializableExtra(ConstantUtil.KEY_AUTHORIZE);
        } catch (NullPointerException ignored) {
        }

        if (token != null) {
            showLoading();
            viewModel.refuseQuote(token.getAccess_token(), quoteID).observe(getViewLifecycleOwner(), success -> {
                dismissLoading(() -> {
                    DialogUtil.showCenterDialog(getContext(), DialogUtil.DIALOG_TYPE.SUCCESS, R.string.rejected);
                    getQuote();
                });
            });
        } else {
            DialogUtil.showCenterDialog(getContext(), DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed);
        }
    }
}
