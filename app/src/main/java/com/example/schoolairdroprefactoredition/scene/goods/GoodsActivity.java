package com.example.schoolairdroprefactoredition.scene.goods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.blankj.utilcode.util.BarUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.ui.components.ButtonSingle;
import com.example.schoolairdroprefactoredition.ui.components.ButtonDouble;
import com.example.schoolairdroprefactoredition.ui.components.GoodsInfo;
import com.example.schoolairdroprefactoredition.ui.components.GoodsPager;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.facebook.shimmer.Shimmer;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

public class GoodsActivity extends ImmersionStatusBarActivity implements ButtonSingle.OnButtonClickListener, ButtonDouble.OnButtonClickListener {

    private static final String ARGS = "GoodsInfoDetail";

    public static void start(Context context, DomainGoodsInfo.DataBean info) {
        Intent intent = new Intent(context, GoodsActivity.class);
        if (info != null)
            intent.putExtra(ARGS, info);
        context.startActivity(intent);
    }

    private DomainGoodsInfo.DataBean mInfo;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar mToolbar = findViewById(R.id.goods_toolbar);
        StatusBarUtil.setTranslucentForImageView(this, 0, mToolbar);
        BarUtils.setNavBarLightMode(this, true);
        BarUtils.setNavBarColor(this, getColor(R.color.primary));
        setSupportActionBar(mToolbar);

        mInfo = (DomainGoodsInfo.DataBean) getIntent().getSerializableExtra(ARGS);

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
        // todo favorite this
    }

    @Override
    public void onSecondButtonClick() {
        // todo buy
    }

    @Override
    public void onButtonClick() {
        // todo add a comment
    }
}
