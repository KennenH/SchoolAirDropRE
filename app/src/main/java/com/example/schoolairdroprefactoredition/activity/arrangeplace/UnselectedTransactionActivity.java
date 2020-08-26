package com.example.schoolairdroprefactoredition.activity.arrangeplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.adapter.UnselectedTransactionRecyclerAdapter;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;

public class UnselectedTransactionActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, UnselectedTransactionActivity.class);
        context.startActivity(intent);
    }

    private UnselectedTransactionViewModel viewModel;

    private TextView mTitle;
    private RecyclerView mRecycler;
    private UnselectedTransactionRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unselected_transaction);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewModel = new ViewModelProvider(this).get(UnselectedTransactionViewModel.class);

        setSupportActionBar(findViewById(R.id.toolbar));
        mTitle = findViewById(R.id.title);
        mRecycler = findViewById(R.id.recycler);

        mAdapter = new UnselectedTransactionRecyclerAdapter();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new MarginItemDecoration(true));
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