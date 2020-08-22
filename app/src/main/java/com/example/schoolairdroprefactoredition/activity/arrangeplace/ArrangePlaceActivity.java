package com.example.schoolairdroprefactoredition.activity.arrangeplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.adapter.ArrangePositionRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.ArrangePositionHeader;
import com.example.schoolairdroprefactoredition.ui.components.Location;

public class ArrangePlaceActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_arrange_place);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCity = findViewById(R.id.city);
        mDistrict = findViewById(R.id.district);
        mSearch = findViewById(R.id.search);
        mHeader = new ArrangePositionHeader(this);

        mSearchRecycler = findViewById(R.id.search_recycler);
        mPOIRecycler = findViewById(R.id.poi_recycler);

        showPOI();

        mSearchRecycler.setLayoutManager(new LinearLayoutManager(this));
        mPOIRecycler.setLayoutManager(new LinearLayoutManager(this));

        mSearchAdapter = new ArrangePositionRecyclerAdapter();
        mPOIAdapter = new ArrangePositionRecyclerAdapter();

        mPOIAdapter.addHeaderView(mHeader);

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