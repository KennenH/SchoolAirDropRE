package com.example.schoolairdroprefactoredition.scene.credit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.ui.adapter.CreditRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.auto.OverDragLayout;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;
import com.jaeger.library.StatusBarUtil;

public class CreditActivity extends AppCompatActivity {

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CreditActivity.class);
        intent.putExtra(ConstantUtil.KEY_USER_INFO, bundle.getSerializable(ConstantUtil.KEY_USER_INFO));
        intent.putExtra(ConstantUtil.KEY_AUTHORIZE, bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE));
        context.startActivity(intent);
    }

    public static final int CREDIT1 = 1;
    public static final int CREDIT2 = 2;
    public static final int CREDIT3 = 3;
    public static final int CREDIT4 = 4;
    public static final int CREDIT5 = 5;

    private static final int NUM_1 = R.drawable.ic_1;
    private static final int NUM_2 = R.drawable.ic_2;
    private static final int NUM_3 = R.drawable.ic_3;
    private static final int NUM_4 = R.drawable.ic_4;
    private static final int NUM_5 = R.drawable.ic_5;

    private static final int LV_1 = R.string.creditLow;
    private static final int LV_2 = R.string.creditFair;
    private static final int LV_3 = R.string.creditGood;
    private static final int LV_4 = R.string.creditGreat;
    private static final int LV_5 = R.string.creditExcellent;

    private static final int BG_HIGH_DARK = R.color.colorCreditGoodDark;
    private static final int BG_HIGH_LIGHT = R.drawable.bg_rounded_credit_good;
    //    private static final int BG_HIGH_GRADIENT = R.drawable.bg_credit_good;
    private static final int BG_HIGH_GRADIENT = R.color.colorCreditGoodDark;

    private static final int BG_LOW_DARK = R.color.colorCreditLowDark;
    private static final int BG_LOW_LIGHT = R.drawable.bg_rounded_credit_low;
    //    private static final int BG_LOW_GRADIENT = R.drawable.bg_credit_low;
    private static final int BG_LOW_GRADIENT = R.color.colorCreditLowDark;

    private CreditViewModel viewModel;

    private ImageView mCreditNum;
    private TextView mCreditLevel;
    private RecyclerView mRecycler;

    private ConstraintLayout mCreditWrapper;
    private OverDragLayout mHistoryWrapper;

    private CreditRecyclerAdapter mAdapter;

    private Bundle bundle;
    private DomainUserInfo.DataBean info;
    private DomainAuthorize token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewModel = new ViewModelProvider(this).get(CreditViewModel.class);

        bundle = getIntent().getExtras();
        if (bundle == null) bundle = new Bundle();
        info = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
        token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        StatusBarUtil.setTranslucentForImageView(this, 0, toolbar);
        setSupportActionBar(toolbar);
        mCreditNum = findViewById(R.id.credit_num);
        mCreditLevel = findViewById(R.id.credit_level);
        mRecycler = findViewById(R.id.credit_recycler);
        mCreditWrapper = findViewById(R.id.credit_wrapper);
        mHistoryWrapper = findViewById(R.id.credit_recycler_wrapper);
        mAdapter = new CreditRecyclerAdapter();

        mRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        mRecycler.addItemDecoration(new MarginItemDecoration());
        mRecycler.setAdapter(mAdapter);

        viewModel.getCreditHistory().observe(this, data -> {
            mAdapter.setList(data);
            mAdapter.notifyDataSetChanged();
            mRecycler.scrollToPosition(0);
        });

        init();
    }

    private void init() {
        int credit = info.getCredit_num();
        switch (credit) {
            case CREDIT1:
                mCreditNum.setImageDrawable(ContextCompat.getDrawable(this, NUM_1));
                mCreditLevel.setText(LV_1);
                mCreditLevel.setBackgroundResource(BG_LOW_LIGHT);
                mCreditLevel.setTextColor(getResources().getColor(BG_LOW_DARK, getTheme()));
                mCreditWrapper.setBackgroundResource(BG_LOW_DARK);
                mHistoryWrapper.setBackgroundResource(BG_LOW_GRADIENT);

                BarUtils.setNavBarColor(this, getResources().getColor(BG_LOW_DARK, getTheme()));
                BarUtils.setNavBarLightMode(this, false);
                break;
            case CREDIT2:
                mCreditNum.setImageDrawable(ContextCompat.getDrawable(this, NUM_2));
                mCreditLevel.setText(LV_2);
                mCreditLevel.setBackgroundResource(BG_LOW_LIGHT);
                mCreditLevel.setTextColor(getResources().getColor(BG_LOW_DARK, getTheme()));
                mCreditWrapper.setBackgroundResource(BG_LOW_DARK);
                mHistoryWrapper.setBackgroundResource(BG_LOW_GRADIENT);

                BarUtils.setNavBarColor(this, getResources().getColor(BG_LOW_DARK, getTheme()));
                BarUtils.setNavBarLightMode(this, false);
                break;
            case CREDIT3:
                mCreditNum.setImageDrawable(ContextCompat.getDrawable(this, NUM_3));
                mCreditLevel.setText(LV_3);
                mCreditLevel.setBackgroundResource(BG_HIGH_LIGHT);
                mCreditWrapper.setBackgroundResource(BG_HIGH_DARK);
                mHistoryWrapper.setBackgroundResource(BG_HIGH_GRADIENT);

                BarUtils.setNavBarColor(this, getResources().getColor(BG_HIGH_DARK, getTheme()));
                BarUtils.setNavBarLightMode(this, false);
                break;
            case CREDIT4:
                mCreditNum.setImageDrawable(ContextCompat.getDrawable(this, NUM_4));
                mCreditLevel.setText(LV_4);
                mCreditLevel.setBackgroundResource(BG_HIGH_LIGHT);
                mCreditWrapper.setBackgroundResource(BG_HIGH_DARK);
                mHistoryWrapper.setBackgroundResource(BG_HIGH_GRADIENT);

                BarUtils.setNavBarColor(this, getResources().getColor(BG_HIGH_DARK, getTheme()));
                BarUtils.setNavBarLightMode(this, false);
                break;
            case CREDIT5:
                mCreditNum.setImageDrawable(ContextCompat.getDrawable(this, NUM_5));
                mCreditLevel.setText(LV_5);
                mCreditLevel.setBackgroundResource(BG_HIGH_LIGHT);
                mCreditWrapper.setBackgroundResource(BG_HIGH_DARK);
                mHistoryWrapper.setBackgroundResource(BG_HIGH_GRADIENT);

                BarUtils.setNavBarColor(this, getResources().getColor(BG_HIGH_DARK, getTheme()));
                BarUtils.setNavBarLightMode(this, false);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}