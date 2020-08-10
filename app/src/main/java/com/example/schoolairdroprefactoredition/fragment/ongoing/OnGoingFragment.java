package com.example.schoolairdroprefactoredition.fragment.ongoing;

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

import com.example.schoolairdroprefactoredition.databinding.FragmentRecyclerBinding;
import com.example.schoolairdroprefactoredition.model.databean.TestOnGoingBean;
import com.example.schoolairdroprefactoredition.ui.adapter.OnGoingRecyclerAdapter;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;

import java.util.List;

public class OnGoingFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int PAGE_UNPAID = 0;
    private static final int PAGE_UNDELIVERED = 1;

    private OnGoingViewModel viewModel;

    private int index;

    private RecyclerView mRecycler;

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
        final FragmentRecyclerBinding binding = FragmentRecyclerBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(OnGoingViewModel.class);
        index = PAGE_UNPAID;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        mRecycler = binding.recycler;
        mAdapter = new OnGoingRecyclerAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecycler.setLayoutManager(manager);

        if (index == PAGE_UNPAID) {
            viewModel.getUnPaid().observe(getViewLifecycleOwner(), unpaid -> {
                mAdapter.setList(unpaid);
                mRecycler.addItemDecoration(new MarginItemDecoration());
                mAdapter.notifyDataSetChanged();
            });
        } else {
            viewModel.getUnDelivered().observe(getViewLifecycleOwner(), undelivered -> {
                mAdapter.setList(undelivered);
                mRecycler.addItemDecoration(new MarginItemDecoration());
                mAdapter.notifyDataSetChanged();
            });
        }
        mRecycler.setAdapter(mAdapter);

        return binding.getRoot();
    }

    private void sortData(List<TestOnGoingBean> data) {

    }
}
