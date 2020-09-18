package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding;
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;

/**
 * {@link SellingFragment}
 * {@link BoughtFragment}
 */
public class SoldFragment extends SSBBaseFragment {

    public static SoldFragment newInstance(Bundle bundle) {
        SoldFragment fragment = new SoldFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void init(FragmentSsbBinding binding) {
        if (token != null) {
            mLoading.setVisibility(View.VISIBLE);
            viewModel.getSold(token.getAccess_token()).observe(getViewLifecycleOwner(), data -> {
                mLoading.setVisibility(View.GONE);

                mList = data.getData();
                mAdapter.setList(mList);

                LogUtils.d(mList.size());
                if (mList.size() == 0)
                    binding.nothing.setVisibility(View.VISIBLE);
                else
                    binding.nothing.setVisibility(View.GONE);
            });
        }

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (getActivity() instanceof SSBActivity) {
                    ((SSBActivity) getActivity()).hideSearchBar();
                }
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