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
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.scene.main.my.MyViewModel;
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
    public static final String KEY_IS_FROM_SELLING = "fromSelling?";

    /**
     * 本页面中不存在KEY_USER_INFO
     * 1、账号本人信息由KEY_AUTHORIZE从服务器获取，但是它可为空即在未登录状态查看物品信息
     * 2、物品以及卖家信息由KEY_GOODS_INFO持有，不可以为空
     *
     * @param token         验证信息
     * @param goodsInfo     物品信息,包含卖家信息
     * @param isFromSelling 详见{@link GoodsInfo#hideSellerInfo()}
     */
    public static void start(Context context, DomainAuthorize token, DomainGoodsInfo.DataBean goodsInfo, boolean isFromSelling) {
        if (goodsInfo == null) return;

        Intent intent = new Intent(context, GoodsActivity.class);
        intent.putExtra(ConstantUtil.KEY_GOODS_INFO, goodsInfo);
        intent.putExtra(ConstantUtil.KEY_AUTHORIZE, token);
        intent.putExtra(KEY_IS_FROM_SELLING, isFromSelling);

        if (context instanceof AppCompatActivity)
            ((AppCompatActivity) context).startActivityForResult(intent, LoginActivity.LOGIN);
        else
            context.startActivity(intent);
    }

    private MyViewModel myViewModel;
    private GoodsViewModel goodsViewModel;

    private ActivityGoodsBinding binding;

    private BottomSheetDialog dialog;

    private Bundle bundle;
    private DomainAuthorize token;
    private DomainGoodsInfo.DataBean goodsInfo;

    private boolean isNotMine = false; // 是否不是我的物品
    private boolean isFavored = false; // 物品是否已收藏

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodsViewModel = new ViewModelProvider(this).get(GoodsViewModel.class);
        goodsViewModel.setOnRequestListener(this);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        binding = ActivityGoodsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        //////////////////////////// 不要改动这里的设置 /////////////////////////////////
        StatusBarUtil.setTranslucentForImageView(this, 0, binding.goodsToolbar);
        BarUtils.setStatusBarLightMode(this, !isDarkTheme);
        BarUtils.setNavBarLightMode(this, !isDarkTheme);
        //////////////////////////// 不要改动这里的设置 /////////////////////////////////

        setSupportActionBar(binding.goodsToolbar);

        bundle = getIntent().getExtras();
        if (bundle == null) bundle = new Bundle();

        binding.goodsButtonLeft.setVisibility(View.GONE);
        binding.goodsButtonRight.setVisibility(View.GONE);

        binding.goodsInfoContainer.setOnUserInfoClickListener(this);
        binding.goodsButtonLeft.setOnButtonClickListener(this);
        binding.goodsButtonRight.setOnButtonClickListener(this);

        validateInfo();
    }

    /**
     * 有效化登录状态等页面信息
     */
    private void validateInfo() {
        token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
        goodsInfo = (DomainGoodsInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_GOODS_INFO);
        // 若从用户信息页面进入的在售页面，则隐藏卖家信息，原因见方法本身
        if (bundle.getBoolean(KEY_IS_FROM_SELLING))
            binding.goodsInfoContainer.hideSellerInfo();

        if (token != null && goodsInfo.getSeller_info() != null) {
            checkIfItemFavored();
            myViewModel.getUserInfo(token.getAccess_token()).observe(this, data -> {
                if (data != null) {
                    isNotMine = data.getData().get(0).getUid() != goodsInfo.getSeller_info().getUid();
                    if (isNotMine) { // 不是我的物品，则显示与卖家互动的按钮
                        binding.goodsButtonLeft.setVisibility(View.VISIBLE);
                        binding.goodsButtonRight.setVisibility(View.VISIBLE);
                        binding.goodsInfoContainer.showBottom();
                    }
                    binding.goodsInfoContainer.stopShimming();
                }
            });
        } else binding.goodsInfoContainer.stopShimming();

        binding.goodsInfoContainer.setData(goodsInfo);
    }

    /**
     * 检查物品是否被收藏
     */
    private void checkIfItemFavored() {
    }

    /**
     * 登录
     */
    public void login() {
        LoginActivity.Companion.startForLogin(this);
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
     * 发起报价
     */
    private void quote(String input) {
        if (token == null) {
            login();
        } else {
            if (goodsInfo != null && isNotMine) {
                showLoading();
                goodsViewModel.quoteRequest(token.getAccess_token(), goodsInfo.getGoods_id(), input)
                        .observe(this, result -> {
                                    if (result != null) {
                                        dismissLoading(() -> {
                                            if (dialog != null)
                                                dialog.dismiss();
                                            DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.SUCCESS, R.string.successQuote);
                                        });
                                    }
                                }
                        );
            } else {
                if (dialog != null)
                    dialog.dismiss();
                DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    /**
     * 收藏或取消收藏物品
     */
    @Override
    public void onLeftButtonClick() {
        if (token == null) {
            login();
        } else {
            if (goodsInfo != null) {
                binding.goodsButtonRight.toggleFavor();
            }
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
                    binding.warning.setVisibility(View.GONE);

                    if (goodsInfo.getGoods_is_brandNew() != 1)
                        binding.secondHandTip.setVisibility(View.VISIBLE);
                    else binding.secondHandTip.setVisibility(View.GONE);

                    if (goodsInfo.getGoods_is_quotable() == 1) {
                        binding.quotePrice.setFilters(new InputFilter[]{new DecimalFilter()});
                        binding.quotePrice.setOnEditorActionListener((v, actionId, event) -> {
                            binding.quotePrice.clearFocus();
                            KeyboardUtils.hideSoftInput(v);
                            return true;
                        });
                        binding.quotePrice.setOnFocusChangeListener((v, hasFocus) -> {
                            if (hasFocus && binding.warning.getVisibility() == View.VISIBLE)
                                AnimUtil.collapse(binding.warning);
                        });
                        binding.notQuotableTip.setVisibility(View.GONE);
                    } else {
                        binding.notQuotableTip.setVisibility(View.VISIBLE);
                        binding.quotePrice.setText(goodsInfo.getGoods_price());
                        binding.quotePrice.setEnabled(false);
                    }

                    binding.originPrice.setText(getString(R.string.priceRMB, goodsInfo.getGoods_price()));
                    binding.title.setOnClickListener(v -> {
                        binding.quotePrice.clearFocus();
                        KeyboardUtils.hideSoftInput(v);
                    });
                    binding.notQuotableTip.setOnClickListener(v -> {
                        binding.quotePrice.clearFocus();
                        KeyboardUtils.hideSoftInput(v);
                    });
                    binding.secondHandTip.setOnClickListener(v -> {
                        binding.quotePrice.clearFocus();
                        KeyboardUtils.hideSoftInput(v);
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

                    binding.quotePrice.clearFocus();

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
        binding.goodsInfoContainer.stopShimming();
        dismissLoading(() -> DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed));
    }

    /**
     * 查看卖家信息
     */
    @Override
    public void onUserInfoClick(View view) {
        UserActivity.start(this, false, token, goodsInfo.getSeller_info());
    }
}
