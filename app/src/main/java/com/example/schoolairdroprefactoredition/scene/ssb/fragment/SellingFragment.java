package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding;
import com.example.schoolairdroprefactoredition.scene.addnew.SellingAddNewActivity;
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

/**
 * {@link SoldFragment}
 * {@link BoughtFragment}
 */
public class SellingFragment extends SSBBaseFragment {

    public static SellingFragment newInstance(Bundle bundle) {
        SellingFragment fragment = new SellingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private long lastClickTime = 0;

    @Override
    protected void init(FragmentSsbBinding binding) {
        setHasOptionsMenu(true);
        if (token != null) {
            mLoading.setVisibility(View.VISIBLE);
            viewModel.getSelling(token.getAccess_token()).observe(getViewLifecycleOwner(), data -> {
                mLoading.setVisibility(View.GONE);

                mList = data.getData();
                mAdapter.setList(mList);
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.ssb, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (SystemClock.elapsedRealtime() - lastClickTime < ConstantUtil.MENU_CLICK_GAP)
            return false;
        lastClickTime = SystemClock.elapsedRealtime();

        int id = item.getItemId();
        if (id == R.id.toolbar) {
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        } else if (id == R.id.ssb_selling_add) {
            if (getActivity() != null) {
                SellingAddNewActivity.start(getContext());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFilterTimeAsc() {
        // 将data按时间正序排列
        /*
         * Sort(mList);
         * mAdapter.setList(mList);
         */
    }

    @Override
    public void onFilterTimeDesc() {
        // 将data按时间倒序排列
    }

    @Override
    public void onFilterWatches() {
        // 将data按浏览量排序
    }
}
