package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.SSBAdapter;
import com.example.schoolairdroprefactoredition.ui.components.SSBFilter;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import java.util.List;

/**
 * {@link SellingFragment}
 * {@link SoldFragment}
 */
public class BoughtFragment extends Fragment implements SSBFilter.OnFilterListener, BaseStateViewModel.OnRequestListener {

    public static BoughtFragment newInstance(Bundle bundle) {
        BoughtFragment fragment = new BoughtFragment();
        fragment.setArguments(new Bundle(bundle));
        return fragment;
    }

    private BoughtViewModel viewModel;

    private RecyclerView mRecycler;
    private SSBAdapter mAdapter;
    private SSBFilter mFilter;

    private List<DomainGoodsInfo.DataBean> mList;

    private Bundle bundle;

    private DomainAuthorize token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();

        token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
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

        if (token != null)
            viewModel.getBoughtBeans(token.getAccess_token()).observe(getViewLifecycleOwner(), data -> {
                mList = data.getData();
                mAdapter.setList(data.getData());

                if (mList.size() == 0)
                    binding.nothing.setVisibility(View.VISIBLE);
                else
                    binding.nothing.setVisibility(View.GONE);
            });

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (getActivity() instanceof SSBActivity)
                    ((SSBActivity) getActivity()).hideSearchBar();
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