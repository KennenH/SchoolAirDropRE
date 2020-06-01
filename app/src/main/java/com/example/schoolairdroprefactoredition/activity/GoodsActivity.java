package com.example.schoolairdroprefactoredition.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.components.ButtonSingle;
import com.example.schoolairdroprefactoredition.ui.components.ButtonDouble;
import com.example.schoolairdroprefactoredition.ui.components.GoodsInfo;
import com.example.schoolairdroprefactoredition.ui.components.GoodsPager;
import com.example.schoolairdroprefactoredition.ui.components.HeaderOnlyRecyclerAdapter;
import com.jaeger.library.StatusBarUtil;

public class GoodsActivity extends AppCompatActivity implements ButtonSingle.OnButtonClickListener, ButtonDouble.OnButtonClickListener {

    private GoodsViewModel goodsViewModel;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_goods);

        goodsViewModel = new ViewModelProvider(this).get(GoodsViewModel.class);

        initView();
        initRecycler();
    }

    private void initView() {
        Toolbar mToolbar = findViewById(R.id.goods_toolbar);
        StatusBarUtil.setTranslucentForImageView(this, 0, mToolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_left_fill, getTheme()));
        }

        ButtonSingle mLeft = findViewById(R.id.goods_button_left);
        ButtonDouble mRight = findViewById(R.id.goods_button_right);
        mLeft.setOnButtonClickListener(this);
        mRight.setOnButtonClickListener(this);
    }

    private void initRecycler() {
        RecyclerView mRecycler = findViewById(R.id.goods_recycler);
        HeaderOnlyRecyclerAdapter mRecyclerAdapter = new HeaderOnlyRecyclerAdapter();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        // instantiate headers
        GoodsPager goodsPager = new GoodsPager(this);
        GoodsInfo goodsInfo = new GoodsInfo(this);

        // get online data
        goodsViewModel.getGoodsData().observe(this, data -> {
            goodsPager.setData(data.getImageData());
            goodsInfo.setData(data);
        });

        // add headers
        mRecyclerAdapter.addHeaderView(goodsPager);
        mRecyclerAdapter.addHeaderView(goodsInfo);

        // load adapter
        mRecycler.setAdapter(mRecyclerAdapter);
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
