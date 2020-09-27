package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding;
import com.example.schoolairdroprefactoredition.databinding.SheetSsbItemMoreBinding;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.addnew.SellingAddNewActivity;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lxj.xpopup.XPopup;

/**
 * {@link SoldFragment}
 * {@link BoughtFragment}
 */
public class SellingFragment extends SSBBaseFragment implements StatePlaceHolder.OnPlaceHolderRefreshListener {

    private BottomSheetDialog dialog;

    public static SellingFragment newInstance(Bundle bundle) {
        SellingFragment fragment = new SellingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private long lastClickTime = 0;

    @Override
    public void init(FragmentSsbBinding binding) {
        setHasOptionsMenu(true);
        getSelling();
    }

    @Override
    public void retryGrabOnlineData() {
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


    /**
     * 在售物品更多设置
     * 修改物品信息 下架物品等
     */
    @Override
    public void onItemAction(View view, DomainGoodsInfo.DataBean bean) {
        if (dialog == null) {
            dialog = new BottomSheetDialog(getContext());
            SheetSsbItemMoreBinding binding = SheetSsbItemMoreBinding.inflate(LayoutInflater.from(getContext()));
            dialog.setContentView(binding.getRoot());
            try {
                binding.modify.setOnClickListener(v -> {

                    dialog.dismiss();
                });

                binding.offShelf.setOnClickListener(v -> {
                    new XPopup.Builder(getContext()).asConfirm(getString(R.string.attention), getString(R.string.unListItem), getString(android.R.string.cancel), getString(android.R.string.ok)
                            , () -> {
                                if (token != null && bean != null) {
                                    viewModel.unListItem(token.getAccess_token(), bean.getGoods_id())
                                            .observe(getViewLifecycleOwner(), result -> {
                                                getSelling();
                                                MyUtil.showCenterDialog(getContext(), MyUtil.DIALOG_TYPE.SUCCESS);
                                            });
                                }
                            }, () -> {
                            }, false).show();
                    dialog.dismiss();
                });

                binding.cancel.setOnClickListener(v -> dialog.dismiss());

                {
                    View view1 = dialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    view1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.transparent, getContext().getTheme()));
                    final BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(view1);
                    bottomSheetBehavior.setSkipCollapsed(true);
                    bottomSheetBehavior.setDraggable(false);
                    bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {
                            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                                dialog.dismiss();
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            }
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        }
                    });
                }
            } catch (NullPointerException ignored) {
            }
        }
        dialog.show();
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
    public void onPlaceHolderRetry(View view) {
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
