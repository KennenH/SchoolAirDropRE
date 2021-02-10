package com.example.schoolairdroprefactoredition.scene.ssb.fragment;

import android.content.Context;
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
import com.example.schoolairdroprefactoredition.domain.HomeGoodsListInfo;
import com.example.schoolairdroprefactoredition.scene.addnew.AddNewActivity;
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DialogUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import static com.example.schoolairdroprefactoredition.domain.DomainUserInfo.DataBean;

/**
 * todo 若查看的为他人的页面，则检查该用户是否开放其他用户查看其在售列表，不允许则显示被隐藏
 */
public class SellingFragment extends SSBBaseFragment implements SSBActivity.OnLoginStateChangeListener {

    private BottomSheetDialog dialog;
    private boolean isMine = false;

    public static SellingFragment newInstance(boolean isMine) {
        SellingFragment fragment = new SellingFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ConstantUtil.KEY_IS_MINE, isMine);
        fragment.setArguments(bundle);
        return fragment;
    }

    private long lastClickTime = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (getArguments() != null) {
            isMine = getArguments().getBoolean(ConstantUtil.KEY_IS_MINE, isMine);
        }

        if (getActivity() instanceof SSBActivity) {
            ((SSBActivity) getActivity()).setOnLoginStateChangeListener(this);
        }
    }

    /**
     * 初始化
     */
    @Override
    public void init(FragmentSsbBinding binding) {
        if (isMine) {
            setHasOptionsMenu(true);
        }
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
     * 1、若为自己的物品则使用token获取物品
     * 2、若为他人的物品则使用用户id获取
     */
    private void getSelling() {
        if (isMine) {
            if (getToken() != null) {
                showPlaceholder(StatePlaceHolder.TYPE_LOADING);
                viewModel.getSelling(15).observe(getViewLifecycleOwner(), data -> {
                    loadData(data);
                    dataLenOnChange(SSBBaseFragment.SELLING_POS);
                });
            } else showPlaceholder(StatePlaceHolder.TYPE_EMPTY);
        } else {
            DataBean info = getUserInfo();
            if (info != null) {
                showPlaceholder(StatePlaceHolder.TYPE_LOADING);
                viewModel.getSellingByID(info.getUserId()).observe(getViewLifecycleOwner(), data -> {
                    loadData(data);
                    dataLenOnChange(SSBBaseFragment.SELLING_POS);
                });
            } else showPlaceholder(StatePlaceHolder.TYPE_ERROR);
        }
    }

    /**
     * 在售物品更多设置
     * 修改物品信息 下架物品等
     */
    @Override
    public void onItemAction(View view, HomeGoodsListInfo.DataBean bean) {
        if (isMine) {
            try {
                dialog = new BottomSheetDialog(getContext());
                SheetSsbItemMoreBinding binding = SheetSsbItemMoreBinding.inflate(LayoutInflater.from(getContext()));
                dialog.setContentView(binding.getRoot());
                binding.modify.setOnClickListener(v -> {
                    AddNewActivity.start(getContext(),bean.getGoods_id());
                    dialog.dismiss();
                });
                binding.offShelf.setOnClickListener(v -> {
                    DialogUtil.showConfirm(getContext(), getString(R.string.attention), getString(R.string.unListItem),
                            () -> {
                                if (getToken() != null && bean != null) {
                                    viewModel.unListItem(getToken().getAccess_token(), String.valueOf(bean.getGoods_id()))
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
            dialog.show();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (isMine) {
            inflater.inflate(R.menu.ssb, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!isMine) {
            return false;
        }

        if (SystemClock.elapsedRealtime() - lastClickTime < ConstantUtil.MENU_CLICK_GAP) {
            return false;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        int id = item.getItemId();
        if (id == R.id.toolbar) {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        } else if (id == R.id.selling_posts_add) {
            if (getActivity() != null && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
                AddNewActivity.start(getContext(), AddNewActivity.AddNewType.ADD_ITEM);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoginSSB() {
        getSelling();
    }
}
