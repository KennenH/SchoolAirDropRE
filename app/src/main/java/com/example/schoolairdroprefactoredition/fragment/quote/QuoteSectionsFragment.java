package com.example.schoolairdroprefactoredition.fragment.quote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolairdroprefactoredition.databinding.FragmentQuoteBinding;
import com.example.schoolairdroprefactoredition.model.databean.TestQuoteSectionItemBean;
import com.example.schoolairdroprefactoredition.ui.adapter.QuoteRecyclerAdapter;
import com.example.schoolairdroprefactoredition.utils.decoration.QuoteDecoration;
import com.example.schoolairdroprefactoredition.utils.decoration.VerticalItemLineDecoration;

import java.util.List;

public class QuoteSectionsFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private QuoteSectionsFragmentViewModel viewModel;

    private int index;

    private RecyclerView mRecycler;

    private QuoteRecyclerAdapter mAdapter;

    public static QuoteSectionsFragment newInstance(int index) {
        QuoteSectionsFragment fragment = new QuoteSectionsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentQuoteBinding binding = FragmentQuoteBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(QuoteSectionsFragmentViewModel.class);
        index = 0;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        mRecycler = binding.quoteList;
        mAdapter = new QuoteRecyclerAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecycler.setLayoutManager(manager);

        if (index == 0) {
            viewModel.getQuoteReceivedSection().observe(getViewLifecycleOwner(), received -> {
                // 将排序后的报价列表分成两类
                mAdapter.setList(received);// 此处还未排序
                mRecycler.addItemDecoration(new QuoteDecoration(getContext(), received.size(), 2));

                mAdapter.notifyDataSetChanged();
            });
        } else {
            viewModel.getQuoteSentSection().observe(getViewLifecycleOwner(), sent -> {
                // 同上
                mAdapter.setList(sent);
                mRecycler.addItemDecoration(new QuoteDecoration(getContext(), sent.size(), 5));

                mAdapter.notifyDataSetChanged();
            });
        }
        mRecycler.setAdapter(mAdapter);

        return binding.getRoot();
    }

    private void sortData(List<TestQuoteSectionItemBean> data) {

    }
}
