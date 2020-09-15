package com.example.schoolairdroprefactoredition.scene.goods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.BarUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity;
import com.example.schoolairdroprefactoredition.ui.components.ButtonDouble;
import com.example.schoolairdroprefactoredition.ui.components.ButtonSingle;
import com.example.schoolairdroprefactoredition.ui.components.GoodsInfo;
import com.example.schoolairdroprefactoredition.ui.components.GoodsPager;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.jaeger.library.StatusBarUtil;

public class GoodsActivity extends ImmersionStatusBarActivity implements ButtonSingle.OnButtonClickListener, ButtonDouble.OnButtonClickListener {

    public static void start(Context context, DomainGoodsInfo.DataBean info) {
        Intent intent = new Intent(context, GoodsActivity.class);
        intent.putExtra(ConstantUtil.KEY_USER_INFO, info);
        context.startActivity(intent);
    }

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

        mInfo = (DomainGoodsInfo.DataBean) getIntent().getSerializableExtra(ConstantUtil.KEY_USER_INFO);

        ButtonSingle mLeft = findViewById(R.id.goods_button_left);
        ButtonDouble mRight = findViewById(R.id.goods_button_right);
        GoodsInfo goodsInfo = findViewById(R.id.goods_info_container);
        GoodsPager goodsPager = findViewById(R.id.goods_pager);

        if (mInfo != null) {
            try {
                goodsInfo.setData(mInfo);
                goodsPager.setData(MyUtil.getArrayFromString(mInfo.getGoods_img_set()));
            } catch (NullPointerException e) {
                Log.d("GoodsActivity", "null pointer exception" + e.toString());
            }
        }

        mLeft.setOnButtonClickListener(this);
        mRight.setOnButtonClickListener(this);
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

    }

    @Override
    public void onButtonClick() {
        // todo chat with seller
        ChatActivity.start(this);
    }
}
