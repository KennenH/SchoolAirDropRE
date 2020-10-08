package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.content.Context;
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
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DialogUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * {@link SoldFragment}
 * {@link BoughtFragment}
 * <p>
 * todo 若查看的为他人的页面，则检查该用户是否开放其他用户查看其在售列表，不允许则显示被拒绝
 */
public class SellingFragment extends SSBBaseFragment implements SSBActivity.OnLoginStateChangeListener {

    private BottomSheetDialog dialog;

    public static SellingFragment newInstance() {
        SellingFragment fragment = new SellingFragment();
        return fragment;
    }

    private long lastClickTime = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() instanceof SSBActivity)
            ((SSBActivity) getActivity()).setOnLoginStateChangeListener(this);
    }

    /**
     * 初始化
     */
    @Override
    public void init(FragmentSsbBinding binding) {
        setHasOptionsMenu(true);
        getSelling();
    }

    /**
     * 重试
     */
    @Override
    public void retryGrabOnlineData() {
        getSelling();
    }

    /**
     * 网络请求在售物品
     */
    private void getSelling() {
        if (getToken() != null) { // 已登录
            showPlaceholder(StatePlaceHolder.TYPE_LOADING);
            viewModel.getSelling(getToken().getAccess_token()).observe(getViewLifecycleOwner(), this::loadData);
        } else // 未登录时
            showPlaceholder(StatePlaceHolder.TYPE_EMPTY);
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
                binding.modify.setOnClickListener(v -> dialog.dismiss());
                binding.offShelf.setOnClickListener(v -> {
                    DialogUtil.showConfirm(getContext(), getString(R.string.attention), getString(R.string.unListItem),
                            () -> {
                                if (getToken() != null && bean != null) {
                                    viewModel.unListItem(getToken().getAccess_token(), bean.getGoods_id())
                                            .observe(getViewLifecycleOwner(), result -> {
                                                getSelling();
                                                DialogUtil.showCenterDialog(getContext(), DialogUtil.DIALOG_TYPE.SUCCESS, R.string.successUnlist);
                                            });
                                } else
                                    DialogUtil.showCenterDialog(getContext(), DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown);
                            });
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
            if (getActivity() != null && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
                SellingAddNewActivity.start(getContext(), getActivity().getIntent().getExtras());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFilterTimeAsc() {
        // 将data按时间正序排列
    }

    @Override
    public void onFilterTimeDesc() {
        // 将data按时间倒序排列
    }

    @Override
    public void onFilterWatches() {
        // 将data按浏览量排序
    }

    @Override
    public void onLoginSSB() {
        getSelling();
    }
}
