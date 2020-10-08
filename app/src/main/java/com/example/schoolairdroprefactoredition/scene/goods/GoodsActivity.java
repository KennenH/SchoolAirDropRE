package com.example.schoolairdroprefactoredition.scene.goods;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ActivityGoodsBinding;
import com.example.schoolairdroprefactoredition.databinding.SheetQuoteBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity;
import com.example.schoolairdroprefactoredition.scene.user.UserActivity;
import com.example.schoolairdroprefactoredition.ui.components.ButtonDouble;
import com.example.schoolairdroprefactoredition.ui.components.ButtonSingle;
import com.example.schoolairdroprefactoredition.ui.components.GoodsInfo;
import com.example.schoolairdroprefactoredition.utils.AnimUtil;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DecimalFilter;
import com.example.schoolairdroprefactoredition.utils.DialogUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jaeger.library.StatusBarUtil;

public class GoodsActivity extends ImmersionStatusBarActivity implements ButtonSingle.OnButtonClickListener, ButtonDouble.OnButtonClickListener, BaseStateViewModel.OnRequestListener, GoodsInfo.OnUserInfoClickListener {
    public static void start(Context context, Bundle bundle, DomainGoodsInfo.DataBean goodsInfo) {
        Intent intent = new Intent(context, GoodsActivity.class);
        intent.putExtras(bundle);
        intent.putExtra(ConstantUtil.KEY_GOODS_INFO, goodsInfo);

        if (context instanceof AppCompatActivity)
            ((AppCompatActivity) context).startActivityForResult(intent, LoginActivity.LOGIN);
        else
            context.startActivity(intent);
    }

    private GoodsViewModel viewModel;

    private ActivityGoodsBinding binding;

    private BottomSheetDialog dialog;

    private Bundle bundle;
    private DomainAuthorize token;
    private DomainGoodsInfo.DataBean goodsInfo;
    private DomainUserInfo.DataBean myInfo;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(GoodsViewModel.class);
        viewModel.setOnRequestListener(this);
        binding = ActivityGoodsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        StatusBarUtil.setTranslucentForImageView(this, 0, binding.goodsToolbar);
        BarUtils.setStatusBarLightMode(this, true);
        BarUtils.setNavBarLightMode(this, true);
        BarUtils.setNavBarColor(this, getColor(R.color.white));
        setSupportActionBar(binding.goodsToolbar);

        bundle = getIntent().getExtras();
        if (bundle == null) bundle = new Bundle();
        validateInfo();

        binding.goodsInfoContainer.setOnUserInfoClickListener(this);
        binding.goodsButtonLeft.setOnButtonClickListener(this);
        binding.goodsButtonRight.setOnButtonClickListener(this);
    }

    /**
     * 若为自己的物品则将页面底部的三个按钮隐藏
     */
    private void validateInfo() {
        token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
        goodsInfo = (DomainGoodsInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_GOODS_INFO);
        myInfo = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);

        if (isMine()) {
            binding.goodsButtonLeft.setVisibility(View.GONE);
            binding.goodsButtonRight.setVisibility(View.GONE);
            binding.goodsInfoContainer.hideBottom();
        }
        binding.goodsInfoContainer.setData(goodsInfo);
    }

    /**
     * 登录
     */
    public void login() {
        LoginActivity.startForLogin(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LoginActivity.LOGIN) {
                if (data != null) {
                    bundle = data.getExtras();
                    if (bundle != null)
                        bundle.putSerializable(ConstantUtil.KEY_GOODS_INFO, goodsInfo);

                    validateInfo();
                    setResult(Activity.RESULT_OK, data);
                }
            }
        }
    }

    /**
     * 判断是否为自己的物品
     * 若是，则隐藏三个按钮以及下面的留白
     * <p>
     * 不隐藏的情况:
     * 页面个人信息为空
     * 卖家与个人信息不为空 但 卖家信息uid与个人信息uid不一致
     */
    private boolean isMine() {
        if (token != null) {
            if (myInfo != null)
                return goodsInfo == null ||
                        goodsInfo.getSeller_info() == null ||
                        goodsInfo.getSeller_info().getUid() == myInfo.getUid();
        }
        return false;
    }

    /**
     * 发起报价
     */
    private void quote(String input) {
        if (token == null) {
            login();
        } else {
            if (goodsInfo != null && !isMine()) {
                showLoading();
                viewModel.quoteRequest(token.getAccess_token(), goodsInfo.getGoods_id(), input)
                        .observe(this, result -> {
                            dismissLoading();
                            DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.SUCCESS, R.string.successQuote);
                        });
            } else
                DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    /**
     * 收藏物品
     */
    @Override
    public void onLeftButtonClick() {
        if (token == null) {
            login();
        } else {
            if (goodsInfo != null) {
                showLoading();
                viewModel.favoriteItem(token.getAccess_token(), goodsInfo.getGoods_id())
                        .observe(this, result -> {
                            dismissLoading();
                            DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.SUCCESS, R.string.successFavorite);
                        });
            } else
                DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown);
        }
    }

    /**
     * 发起报价
     */
    @Override
    public void onRightButtonClick() {
        if (token == null) {
            login();
        } else {
            if (dialog == null) {
                dialog = new BottomSheetDialog(this);
                SheetQuoteBinding binding = SheetQuoteBinding.inflate(LayoutInflater.from(this));
                dialog.setContentView(binding.getRoot());

                try {
                    binding.title.setOnClickListener(v -> {
                        binding.quotePrice.clearFocus();
                        KeyboardUtils.hideSoftInput(v);
                    });
                    binding.secondHandTip.setOnClickListener(v -> {
                        binding.quotePrice.clearFocus();
                        KeyboardUtils.hideSoftInput(v);
                    });
                    binding.quotePrice.setOnEditorActionListener((v, actionId, event) -> {
                        binding.quotePrice.clearFocus();
                        KeyboardUtils.hideSoftInput(v);
                        return true;
                    });
                    binding.quotePrice.setFilters(new InputFilter[]{new DecimalFilter(5, 2)});
                    binding.quotePrice.setOnFocusChangeListener((v, hasFocus) -> {
                        if (hasFocus && binding.warning.getVisibility() == View.VISIBLE)
                            AnimUtil.collapse(binding.warning);
                    });
                    binding.cancel.setOnClickListener(v -> dialog.dismiss());
                    binding.confirm.setOnClickListener(v -> {
                        if (binding.quotePrice.getText().toString().equals("")) {
                            if (binding.warning.getVisibility() != View.VISIBLE)
                                AnimUtil.expand(binding.warning);
                            else
                                AnimUtil.viewBlink(this, binding.warning, R.color.colorPrimaryRed, R.color.white);
                        } else
                            quote(binding.quotePrice.getText().toString());
                    });

                    {
                        View view1 = dialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
                        view1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.transparent, this.getTheme()));
                        final BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(view1);
                        bottomSheetBehavior.setSkipCollapsed(true);
                        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                            @Override
                            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                                    dialog.dismiss();
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                } else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                                    binding.quotePrice.clearFocus();
                                    KeyboardUtils.hideSoftInput(bottomSheet);
                                }
                            }

                            @Override
                            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                            }
                        });
                    }
                } catch (NullPointerException e) {
                    DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown);
                }
            }
            dialog.show();
        }
    }

    /**
     * 发送消息
     */
    @Override
    public void onButtonClick() {
        if (token == null) {
            login();
        } else
            ChatActivity.start(this);
    }

    @Override
    public void onError() {
        DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed);
    }

    /**
     * 查看卖家信息
     */
    @Override
    public void onUserInfoClick(View view) {
        UserActivity.startForResult(this, bundle, false);
    }
}
