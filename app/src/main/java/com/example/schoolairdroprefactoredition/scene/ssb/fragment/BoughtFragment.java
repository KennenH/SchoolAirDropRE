package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

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
        if (token != null) {
            mLoading.setVisibility(View.VISIBLE);
            viewModel.getBought(token.getAccess_token()).observe(getViewLifecycleOwner(), data -> {
                mLoading.setVisibility(View.GONE);

                mList = data.getData();
                mAdapter.setList(data.getData());

                if (mList.size() == 0)
                    binding.nothing.setVisibility(View.VISIBLE);
                else
                    binding.nothing.setVisibility(View.GONE);
            });
        }

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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