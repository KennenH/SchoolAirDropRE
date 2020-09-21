package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding;
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;

/**
 * {@link SellingFragment}
 * {@link SoldFragment}
 */
public class BoughtFragment extends SSBBaseFragment {

    public static BoughtFragment newInstance(Bundle bundle) {
        BoughtFragment fragment = new BoughtFragment();
        fragment.setArguments(new Bundle(bundle));
        return fragment;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void init(FragmentSsbBinding binding) {
        setHasOptionsMenu(false);
        if (token != null) {
            viewModel.getBought(token.getAccess_token()).observe(getViewLifecycleOwner(), data -> {
                mList = data.getData();
                mAdapter.setList(data.getData());

                if (mList.size() == 0) {

                } else {
                }
            });
        }

        binding.ssbRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (getActivity() instanceof SSBActivity)
                    ((SSBActivity) getActivity()).hideSearchBar();
            }
        });
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
}