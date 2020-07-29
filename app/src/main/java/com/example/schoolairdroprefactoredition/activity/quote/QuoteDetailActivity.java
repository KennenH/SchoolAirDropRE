package com.example.schoolairdroprefactoredition.activity.quote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.components.HeaderOnlyRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.QuoteGoodsSimpleProfile;
import com.example.schoolairdroprefactoredition.ui.components.QuoteOrderInfo;
import com.example.schoolairdroprefactoredition.ui.components.QuoteStatus;

public class QuoteDetailActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, QuoteDetailActivity.class);
        context.startActivity(intent);
    }

    private RecyclerView mRecycler;
    private HeaderOnlyRecyclerAdapter mAdapter;

    private QuoteStatus mStatus;
    private QuoteGoodsSimpleProfile mGoodsProfile;
    private QuoteOrderInfo mOrderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_detail);
        QuoteDetailViewModel viewModel = new ViewModelProvider(this).get(QuoteDetailViewModel.class);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecycler = findViewById(R.id.quote_detail_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HeaderOnlyRecyclerAdapter();

        mStatus = new QuoteStatus(this);
        mGoodsProfile = new QuoteGoodsSimpleProfile(this);
        mOrderInfo = new QuoteOrderInfo(this);
        mAdapter.addHeaderView(mStatus);
        mAdapter.addHeaderView(mGoodsProfile);
        mAdapter.addHeaderView(mOrderInfo);

        viewModel.getQuoteDetail().observe(this, data -> {
            mStatus.setStatus(data.getQuoteStatus());
            mGoodsProfile.setGoodsProfile(data);
            mOrderInfo.setOrderInfo(data);

            mAdapter.notifyDataSetChanged();
        });

        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


}