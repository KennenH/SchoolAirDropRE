package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.ui.adapter.SSBAdapter;
import com.example.schoolairdroprefactoredition.ui.components.SSBFilter;

import java.util.List;

/**
 * {@link SellingFragment}
 * {@link BoughtFragment}
 */
public class SoldFragment extends Fragment implements SSBFilter.OnFilterListener, BaseChildFragmentViewModel.OnRequestListener {

    public static SoldFragment newInstance(String param1, String param2) {
        SoldFragment fragment = new SoldFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private SoldViewModel viewModel;

    private RecyclerView mRecycler;
    private SSBAdapter mAdapter;

    private SSBFilter mFilter;

    private List<TestSSBItemBean> mList;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SoldFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding binding
                = com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SoldViewModel.class);
        viewModel.setOnRequestListener(this);

        mRecycler = binding.ssbRecycler;
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        mFilter = new SSBFilter(getContext());
        mAdapter = new SSBAdapter();

        mFilter.setOnFilterListener(this);
        mAdapter.addHeaderView(mFilter);
        mRecycler.setAdapter(mAdapter);

        viewModel.getSoldBeans().observe(getViewLifecycleOwner(), data -> {
            mList = data;
            mAdapter.setList(data);
        });

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (getActivity() instanceof SSBActivity) {
                    ((SSBActivity) getActivity()).hideSearchBar();
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onFilterTimeAsc() {

    }

    @Override
    public void onFilterTimeDesc() {

    }

    @Override
    public void onFilterWatches() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }
}