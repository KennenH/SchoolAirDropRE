package com.example.schoolairdroprefactoredition.fragment.ssb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.activity.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.fragment.home.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.ui.adapter.SSBAdapter;
import com.example.schoolairdroprefactoredition.ui.components.SSBFilter;

import java.util.List;

/**
 * {@link SellingFragment}
 * {@link SoldFragment}
 */
public class BoughtFragment extends Fragment implements SSBFilter.OnFilterListener, BaseChildFragmentViewModel.OnRequestListener {

    private BoughtViewModel viewModel;

    private RecyclerView mRecycler;
    private SSBAdapter mAdapter;
    private SSBFilter mFilter;

    private List<TestSSBItemBean> mList;

    private Bundle bundle;

    public BoughtFragment() {
        // Required empty public constructor
    }

    public static BoughtFragment newInstance(Bundle bundle) {
        BoughtFragment fragment = new BoughtFragment();
        fragment.setArguments(new Bundle(bundle));
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        bundle = getArguments();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding binding
                = com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(BoughtViewModel.class);
        viewModel.setOnRequestListener(this);

        mRecycler = binding.ssbRecycler;
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        mFilter = new SSBFilter(getContext());
        mAdapter = new SSBAdapter();

        mFilter.setOnFilterListener(this);
        mAdapter.addHeaderView(mFilter);
        mRecycler.setAdapter(mAdapter);

        viewModel.getBoughtBeans().observe(getViewLifecycleOwner(), data -> {
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