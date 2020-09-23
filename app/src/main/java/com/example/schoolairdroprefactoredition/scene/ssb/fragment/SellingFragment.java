package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding;
import com.example.schoolairdroprefactoredition.scene.addnew.SellingAddNewActivity;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

/**
 * {@link SoldFragment}
 * {@link BoughtFragment}
 */
public class SellingFragment extends SSBBaseFragment implements StatePlaceHolder.OnPlaceHolderRefreshListener {

    public static SellingFragment newInstance(Bundle bundle) {
        SellingFragment fragment = new SellingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private long lastClickTime = 0;

    @Override
    protected void init(FragmentSsbBinding binding) {
        setHasOptionsMenu(true);
        getSelling();
    }

    /**
     * 网络请求在售物品
     */
    private void getSelling() {
        if (token != null) { // 已登录
            showPlaceHolder(StatePlaceHolder.TYPE_LOADING);
            viewModel.getSelling(token.getAccess_token()).observe(getViewLifecycleOwner(), this::loadData);
        } else // 未登录时
            showPlaceHolder(StatePlaceHolder.TYPE_EMPTY);
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
                SellingAddNewActivity.start(getContext(), bundle);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRetry(View view) {
        getSelling();
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
