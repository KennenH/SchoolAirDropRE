package com.example.schoolairdroprefactoredition.scene.goods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ActivityGoodsBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.ui.components.ButtonDouble;
import com.example.schoolairdroprefactoredition.ui.components.ButtonSingle;
import com.example.schoolairdroprefactoredition.utils.AnimUtil;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DecimalFilter;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jaeger.library.StatusBarUtil;

public class GoodsActivity extends ImmersionStatusBarActivity implements ButtonSingle.OnButtonClickListener, ButtonDouble.OnButtonClickListener, BaseStateViewModel.OnRequestListener {

    public static void start(Context context, Bundle bundle, DomainGoodsInfo.DataBean goodsInfo) {
        Intent intent = new Intent(context, GoodsActivity.class);
        intent.putExtras(bundle);
        intent.putExtra(ConstantUtil.KEY_GOODS_INFO, goodsInfo);
        context.startActivity(intent);
    }

    private GoodsViewModel viewModel;

    private ActivityGoodsBinding binding;

    private BottomSheetDialog dialog;

    private Bundle bundle;
    private DomainAuthorize token;
    private DomainGoodsInfo.DataBean goodsInfo;
    private DomainUserInfo.DataBean info;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(GoodsViewModel.class);
        viewModel.setOnRequestListener(this);
        binding = ActivityGoodsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        StatusBarUtil.setTranslucentForImageView(this, 0, binding.goodsToolbar);
        BarUtils.setNavBarLightMode(this, true);
        BarUtils.setNavBarColor(this, getColor(R.color.primary));
        setSupportActionBar(binding.goodsToolbar);

        bundle = getIntent().getExtras();
        if (bundle == null) bundle = new Bundle();
        token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
        goodsInfo = (DomainGoodsInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_GOODS_INFO);
        info = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);

        hideActionButtonsIfMyGoods();

        binding.goodsInfoContainer.setData(goodsInfo);
        binding.goodsButtonLeft.setOnButtonClickListener(this);
        binding.goodsButtonRight.setOnButtonClickListener(this);
    }

    /**
     * 判断是否为自己的物品
     * 若是，则隐藏三个按钮以及下面的留白
     * <p>
     * 不隐藏的情况:
     * 页面个人信息为空
     * 卖家与个人信息不为空 但 卖家信息uid与个人信息uid不一致
     */
    private void hideActionButtonsIfMyGoods() {
        if (goodsInfo != null && info != null) {
            DomainGoodsInfo.DataBean.SellerInfoBean seller = goodsInfo.getSeller_info();
            if (seller == null || seller.getUid() == info.getUid()) {
                binding.goodsButtonLeft.setVisibility(View.GONE);
                binding.goodsButtonRight.setVisibility(View.GONE);
                binding.goodsInfoContainer.hideBottom();
            }
        }
    }

    /**
     * 发起报价
     */
    private void quote(String input) {
        if (token != null && goodsInfo != null) {
            showLoading();
            viewModel.quoteRequest(token.getAccess_token(), goodsInfo.getGoods_id(), input)
                    .observe(this, result -> {
                        dismissLoading();
                        MyUtil.showCenterDialog(this, MyUtil.DIALOG_TYPE.SUCCESS);
                    });
        } else
            MyUtil.showCenterDialog(this, MyUtil.DIALOG_TYPE.ERROR_UNKNOWN);
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
    public void onFirstButtonClick() {
        if (token != null && goodsInfo != null) {
            showLoading();
            viewModel.favoriteItem(token.getAccess_token(), goodsInfo.getGoods_id())
                    .observe(this, result -> {
                        dismissLoading();
                        MyUtil.showCenterDialog(this, MyUtil.DIALOG_TYPE.SUCCESS);
                    });
        } else
            MyUtil.showCenterDialog(this, MyUtil.DIALOG_TYPE.ERROR_UNKNOWN);
    }

    /**
     * 发起报价
     */
    @Override
    public void onSecondButtonClick() {
        if (dialog == null) {
            dialog = new BottomSheetDialog(this);
            dialog.setContentView(LayoutInflater.from(this).inflate(R.layout.sheet_quote, null));

            try {
                EditText quote = dialog.findViewById(R.id.quote_price);
                TextView tip = dialog.findViewById(R.id.warning);
                dialog.findViewById(R.id.title).setOnClickListener(v -> {
                    quote.clearFocus();
                    KeyboardUtils.hideSoftInput(v);
                });
                dialog.findViewById(R.id.second_hand_tip).setOnClickListener(v -> {
                    quote.clearFocus();
                    KeyboardUtils.hideSoftInput(v);
                });
                quote.setOnEditorActionListener((v, actionId, event) -> {
                    quote.clearFocus();
                    KeyboardUtils.hideSoftInput(v);
                    return true;
                });
                quote.setFilters(new InputFilter[]{new DecimalFilter(5, 2)});
                quote.setOnFocusChangeListener((v, hasFocus) -> {
                    if (hasFocus && tip.getVisibility() == View.VISIBLE)
                        AnimUtil.collapse(tip);
                });
                dialog.findViewById(R.id.cancel).setOnClickListener(v -> dialog.dismiss());
                dialog.findViewById(R.id.confirm).setOnClickListener(v -> {
                    if (quote.getText().toString().equals("")) {
                        if (tip.getVisibility() != View.VISIBLE)
                            AnimUtil.expand(tip);
                        else
                            AnimUtil.viewBlink(this, tip, R.color.colorPrimaryRed, R.color.white);
                    } else
                        quote(quote.getText().toString());
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
                                quote.clearFocus();
                                KeyboardUtils.hideSoftInput(bottomSheet);
                            }
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        }
                    });
                }
            } catch (NullPointerException e) {
                LogUtils.d("dialog null");
            }
        }
        dialog.show();
    }

    /**
     * 发送消息
     */
    @Override
    public void onButtonClick() {
        ChatActivity.start(this);
    }

    @Override
    public void onError() {
        MyUtil.showCenterDialog(this, MyUtil.DIALOG_TYPE.FAILED);
    }
}
