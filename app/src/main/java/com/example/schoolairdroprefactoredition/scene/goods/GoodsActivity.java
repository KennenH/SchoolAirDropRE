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
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity;
import com.example.schoolairdroprefactoredition.ui.components.ButtonDouble;
import com.example.schoolairdroprefactoredition.ui.components.ButtonSingle;
import com.example.schoolairdroprefactoredition.ui.components.GoodsInfo;
import com.example.schoolairdroprefactoredition.ui.components.GoodsPager;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DecimalFilter;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jaeger.library.StatusBarUtil;

public class GoodsActivity extends ImmersionStatusBarActivity implements ButtonSingle.OnButtonClickListener, ButtonDouble.OnButtonClickListener {

    public static void start(Context context, DomainGoodsInfo.DataBean info) {
        Intent intent = new Intent(context, GoodsActivity.class);
        intent.putExtra(ConstantUtil.KEY_GOODS_INFO, info);
        context.startActivity(intent);
    }

    private BottomSheetDialog dialog;

    private DomainGoodsInfo.DataBean mInfo;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        Toolbar mToolbar = findViewById(R.id.goods_toolbar);
        StatusBarUtil.setTranslucentForImageView(this, 0, mToolbar);
        BarUtils.setNavBarLightMode(this, true);
        BarUtils.setNavBarColor(this, getColor(R.color.primary));
        setSupportActionBar(mToolbar);

        mInfo = (DomainGoodsInfo.DataBean) getIntent().getSerializableExtra(ConstantUtil.KEY_GOODS_INFO);

        ButtonSingle mLeft = findViewById(R.id.goods_button_left);
        ButtonDouble mRight = findViewById(R.id.goods_button_right);
        GoodsInfo goodsInfo = findViewById(R.id.goods_info_container);
        GoodsPager goodsPager = findViewById(R.id.goods_pager);

        try {
            goodsInfo.setData(mInfo);
            goodsPager.setData(MyUtil.getArrayFromString(mInfo.getGoods_img_set()));
        } catch (NullPointerException e) {
            LogUtils.d("goods info null");
        }

        mLeft.setOnButtonClickListener(this);
        mRight.setOnButtonClickListener(this);
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
