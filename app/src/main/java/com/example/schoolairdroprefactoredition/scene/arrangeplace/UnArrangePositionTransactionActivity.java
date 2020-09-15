package com.example.schoolairdroprefactoredition.scene.arrangeplace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.UnselectedTransactionRecyclerAdapter;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;

public class UnArrangePositionTransactionActivity extends ImmersionStatusBarActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, UnArrangePositionTransactionActivity.class);
        context.startActivity(intent);
    }

    private UnArrangePositionTransactionViewModel viewModel;

    private TextView mTitle;
    private RecyclerView mRecycler;
    private UnselectedTransactionRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unselected_transaction);
        viewModel = new ViewModelProvider(this).get(UnArrangePositionTransactionViewModel.class);

        setSupportActionBar(findViewById(R.id.toolbar));
        mTitle = findViewById(R.id.title);
        mRecycler = findViewById(R.id.recycler);

        mAdapter = new UnselectedTransactionRecyclerAdapter();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new MarginItemDecoration());
        mRecycler.setAdapter(mAdapter);

        viewModel.getUnselectedTransactionList().observe(this, list -> {
            if (list.size() > 0) {
                mTitle.setText(getString(R.string.toBeAgreedPosition, "小鱼子酱"));
                mAdapter.setList(list);
            } else
                mTitle.setText(getString(R.string.noItemToAgree, "小鱼子酱"));
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }


}