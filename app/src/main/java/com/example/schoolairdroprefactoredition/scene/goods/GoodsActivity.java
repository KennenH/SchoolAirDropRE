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

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ActivityGoodsBinding;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity;
import com.example.schoolairdroprefactoredition.ui.components.ButtonDouble;
import com.example.schoolairdroprefactoredition.ui.components.ButtonSingle;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DecimalFilter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jaeger.library.StatusBarUtil;

public class GoodsActivity extends ImmersionStatusBarActivity implements ButtonSingle.OnButtonClickListener, ButtonDouble.OnButtonClickListener {

    /**
     * @param goodsInfo 当其中的seller_info为空时表明为在售列表，隐藏bottom
     * @param myInfo    当为空时无法判断，因此不隐藏bottom
     */
    public static void start(Context context, DomainGoodsInfo.DataBean goodsInfo, DomainUserInfo.DataBean myInfo) {
        Intent intent = new Intent(context, GoodsActivity.class);
        intent.putExtra(ConstantUtil.KEY_GOODS_INFO, goodsInfo);
        intent.putExtra(ConstantUtil.KEY_USER_INFO, myInfo);
        context.startActivity(intent);
    }

    private ActivityGoodsBinding binding;

    private BottomSheetDialog dialog;

    private DomainGoodsInfo.DataBean mGoodsInfo;
    private DomainUserInfo.DataBean mMyInfo;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoodsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        StatusBarUtil.setTranslucentForImageView(this, 0, binding.goodsToolbar);
        BarUtils.setNavBarLightMode(this, true);
        BarUtils.setNavBarColor(this, getColor(R.color.primary));
        setSupportActionBar(binding.goodsToolbar);

        mGoodsInfo = (DomainGoodsInfo.DataBean) getIntent().getSerializableExtra(ConstantUtil.KEY_GOODS_INFO);
        mMyInfo = (DomainUserInfo.DataBean) getIntent().getSerializableExtra(ConstantUtil.KEY_USER_INFO);

        hideActionButtonsIfMyGoods();

        binding.goodsInfoContainer.setData(mGoodsInfo);
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
        if (mGoodsInfo != null && mMyInfo != null) {
            DomainGoodsInfo.DataBean.SellerInfoBean seller = mGoodsInfo.getSeller_info();
            if (seller == null || seller.getUid() == mMyInfo.getUid()) {
                binding.goodsButtonLeft.setVisibility(View.GONE);
                binding.goodsButtonRight.setVisibility(View.GONE);
                binding.goodsInfoContainer.hideBottom();
            }
        }
    }

    /**
     * 显示发起报价底部弹窗
     */
    private void showQuoteDialog() {
        if (dialog == null) {
            dialog = new BottomSheetDialog(this);
            dialog.setContentView(LayoutInflater.from(this).inflate(R.layout.sheet_quote, null));

            try {
                EditText quote = dialog.findViewById(R.id.quote_price);
                quote.setOnEditorActionListener((v, actionId, event) -> {
                    quote.clearFocus();
                    KeyboardUtils.hideSoftInput(this);
                    return true;
                });
                quote.setFilters(new InputFilter[]{new DecimalFilter(5, 2)});
                dialog.findViewById(R.id.cancel).setOnClickListener(v -> dialog.dismiss());
                dialog.findViewById(R.id.confirm).setOnClickListener(v -> quote(quote.getText().toString()));

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

    /**
     * 发起报价
     */
    private void quote(String input) {
        LogUtils.d(input);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFirstButtonClick() {
        // todo favorite this goods

    }

    @Override
    public void onSecondButtonClick() {
        // todo quote
        showQuoteDialog();
    }

    @Override
    public void onButtonClick() {
        // todo chat with seller
        ChatActivity.start(this);
    }
}
