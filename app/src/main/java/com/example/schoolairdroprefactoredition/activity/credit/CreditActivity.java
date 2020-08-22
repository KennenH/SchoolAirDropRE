package com.example.schoolairdroprefactoredition.activity.credit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.adapter.CreditRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.auto.OverDragLayout;
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;

public class CreditActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, CreditActivity.class);
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
    private static final int BG_HIGH_GRADIENT = R.drawable.bg_credit_good;

    private static final int BG_LOW_DARK = R.color.colorCreditLowDark;
    private static final int BG_LOW_LIGHT = R.drawable.bg_rounded_credit_low;
    private static final int BG_LOW_GRADIENT = R.drawable.bg_credit_low;

    private CreditViewModel viewModel;

    private ImageView mCreditNum;
    private TextView mCreditLevel;
    private RecyclerView mRecycler;

    private ConstraintLayout mCreditWrapper;
    private OverDragLayout mHistoryWrapper;

    private CreditRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        viewModel = new ViewModelProvider(this).get(CreditViewModel.class);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mCreditNum = findViewById(R.id.credit_num);
        mCreditLevel = findViewById(R.id.credit_level);
        mRecycler = findViewById(R.id.credit_recycler);
        mCreditWrapper = findViewById(R.id.credit_wrapper);
        mHistoryWrapper = findViewById(R.id.credit_recycler_wrapper);
        mAdapter = new CreditRecyclerAdapter();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        StatusBarUtil.setTranslucentForImageView(this, 0, toolbar);

        mRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        mRecycler.addItemDecoration(new MarginItemDecoration());
        mRecycler.setAdapter(mAdapter);

        viewModel.getCredit().observe(this, data -> {
            switch (data.getCredits()) {
                case CREDIT1:
                    mCreditNum.setImageDrawable(getDrawable(NUM_1));
                    mCreditLevel.setText(LV_1);
                    mCreditLevel.setBackgroundResource(BG_LOW_LIGHT);
                    mCreditWrapper.setBackgroundResource(BG_LOW_DARK);
                    mHistoryWrapper.setBackgroundResource(BG_LOW_GRADIENT);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//                        mCreditWrapper.setBackgroundColor(getResources().getColor(BG_Low_DARK, getTheme()));
//                    else
//                        mCreditWrapper.setBackgroundColor(getResources().getColor(BG_Low_DARK));
                    break;
                case CREDIT2:
                    mCreditNum.setImageDrawable(getDrawable(NUM_2));
                    mCreditLevel.setText(LV_2);
                    mCreditLevel.setBackgroundResource(BG_LOW_LIGHT);
                    mCreditWrapper.setBackgroundResource(BG_LOW_DARK);
                    mHistoryWrapper.setBackgroundResource(BG_LOW_GRADIENT);
                    break;
                case CREDIT3:
                    mCreditNum.setImageDrawable(getDrawable(NUM_3));
                    mCreditLevel.setText(LV_3);
                    mCreditLevel.setBackgroundResource(BG_HIGH_LIGHT);
                    mCreditWrapper.setBackgroundResource(BG_HIGH_DARK);
                    mHistoryWrapper.setBackgroundResource(BG_HIGH_GRADIENT);
                    break;
                case CREDIT4:
                    mCreditNum.setImageDrawable(getDrawable(NUM_4));
                    mCreditLevel.setText(LV_4);
                    mCreditLevel.setBackgroundResource(BG_HIGH_LIGHT);
                    mCreditWrapper.setBackgroundResource(BG_HIGH_DARK);
                    mHistoryWrapper.setBackgroundResource(BG_HIGH_GRADIENT);
                    break;
                case CREDIT5:
                    mCreditNum.setImageDrawable(getDrawable(NUM_5));
                    mCreditLevel.setText(LV_5);
                    mCreditLevel.setBackgroundResource(BG_HIGH_LIGHT);
                    mCreditWrapper.setBackgroundResource(BG_HIGH_DARK);
                    mHistoryWrapper.setBackgroundResource(BG_HIGH_GRADIENT);
                    break;
            }
        });

        viewModel.getCreditHistory().observe(this, data -> {
            mAdapter.setList(data);
            mAdapter.notifyDataSetChanged();
            mRecycler.smoothScrollToPosition(0);
        });


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