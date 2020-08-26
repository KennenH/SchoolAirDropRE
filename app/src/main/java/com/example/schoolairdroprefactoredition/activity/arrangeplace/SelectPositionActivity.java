package com.example.schoolairdroprefactoredition.activity.arrangeplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.adapter.ArrangePositionRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.ArrangePositionHeader;
import com.example.schoolairdroprefactoredition.ui.components.Location;

public class SelectPositionActivity extends AppCompatActivity {

    public static void startForResult(Context context, int requestCode) {
        Intent intent = new Intent(context, SelectPositionActivity.class);
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).startActivityForResult(intent, requestCode);
        }
    }

    private Location mCity;
    private EditText mSearch;
    private TextView mDistrict;
    private ArrangePositionHeader mHeader;

    private RecyclerView mSearchRecycler;
    private RecyclerView mPOIRecycler;

    private ArrangePositionRecyclerAdapter mSearchAdapter;
    private ArrangePositionRecyclerAdapter mPOIAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_position);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCity = findViewById(R.id.city);
        mDistrict = findViewById(R.id.district);
        mSearch = findViewById(R.id.search);

        mSearchRecycler = findViewById(R.id.search_recycler);
        mPOIRecycler = findViewById(R.id.poi_recycler);

        showPOI();

        mSearchRecycler.setLayoutManager(new LinearLayoutManager(this));
        mPOIRecycler.setLayoutManager(new LinearLayoutManager(this));

        mSearchAdapter = new ArrangePositionRecyclerAdapter();
        mPOIAdapter = new ArrangePositionRecyclerAdapter();

        mHeader = new ArrangePositionHeader(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int marginSE = (int) getResources().getDimension(R.dimen.general_padding_bit_larger);
        final int marginTB = (int) getResources().getDimension(R.dimen.general_padding);
        params.setMargins(marginSE, marginTB, marginSE, marginTB);
        mHeader.setLayoutParams(params);
        mPOIAdapter.addHeaderView(mHeader);

        mSearchRecycler.setAdapter(mSearchAdapter);
        mPOIRecycler.setAdapter(mPOIAdapter);
    }

    private void showPOI() {
        mSearchRecycler.setVisibility(View.GONE);
        mPOIRecycler.setVisibility(View.VISIBLE);
    }

    private void showSearch() {
        mSearchRecycler.setVisibility(View.VISIBLE);
        mPOIRecycler.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }


}