package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
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

        if (getActivity() instanceof MainActivity)
            LogUtils.d("instanceof main");

        setHasOptionsMenu(false);
        if (token != null) {
            viewModel.getSold(token.getAccess_token()).observe(getViewLifecycleOwner(), data -> {
                mList = data.getData();
                mAdapter.setList(mList);

                if (mList.size() == 0) {

                } else {
                }

            });
        }

        binding.ssbRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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