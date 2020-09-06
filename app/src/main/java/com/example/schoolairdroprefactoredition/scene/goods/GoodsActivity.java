package com.example.schoolairdroprefactoredition.scene.goods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.ui.components.ButtonSingle;
import com.example.schoolairdroprefactoredition.ui.components.ButtonDouble;
import com.example.schoolairdroprefactoredition.ui.components.GoodsInfo;
import com.example.schoolairdroprefactoredition.ui.components.GoodsPager;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

public class GoodsActivity extends AppCompatActivity implements ButtonSingle.OnButtonClickListener, ButtonDouble.OnButtonClickListener {

    private static final String ARGS = "GoodsInfo";

    public static void start(Context context, DomainGoodsInfo.DataBean info) {
        Intent intent = new Intent(context, GoodsActivity.class);
        if (info != null)
            intent.putExtra(ARGS, info);
        context.startActivity(intent);
    }

    //    private GoodsViewModel goodsViewModel;
    private DomainGoodsInfo.DataBean mInfo;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar mToolbar = findViewById(R.id.goods_toolbar);
        StatusBarUtil.setTranslucentForImageView(this, 0, mToolbar);
        setSupportActionBar(mToolbar);

        mInfo = (DomainGoodsInfo.DataBean) getIntent().getSerializableExtra(ARGS);
//        goodsViewModel = new ViewModelProvider(this).get(GoodsViewModel.class);

        ButtonSingle mLeft = findViewById(R.id.goods_button_left);
        ButtonDouble mRight = findViewById(R.id.goods_button_right);
        GoodsInfo goodsInfo = findViewById(R.id.goods_info_container);
        GoodsPager goodsPager = findViewById(R.id.goods_pager);

        goodsInfo.setData(mInfo);
        goodsPager.setData(new ArrayList<>());

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
