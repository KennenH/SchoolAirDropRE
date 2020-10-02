package com.example.schoolairdroprefactoredition.scene.arrangeplace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.blankj.utilcode.constant.PermissionConstants;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ActivitySelectPositionBinding;
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.ArrangePositionRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.ArrangePositionHeader;
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView;
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder;

public class ArrangePositionActivity extends PermissionBaseActivity implements PoiSearch.OnPoiSearchListener, AMapLocationListener, StatePlaceHolder.OnPlaceHolderRefreshListener, EndlessRecyclerView.OnLoadMoreListener {

    public static final int SELECT_POSITION = 3453;
    public static final String SELECT_POSITION_KEY = "select_position";

    public static void startForResult(Context context) {
        Intent intent = new Intent(context, ArrangePositionActivity.class);
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).startActivityForResult(intent, SELECT_POSITION);
        }
    }

    private AMapLocationClient mClient;
    private AMapLocationClientOption mOption;
    private AMapLocation mAmapLocation;

    private ActivitySelectPositionBinding binding;

    private ArrangePositionHeader mHeader;

    private ArrangePositionRecyclerAdapter mSearchAdapter;
    private ArrangePositionRecyclerAdapter mPOIAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectPositionBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showPOI();

        binding.placeholder.setOnPlaceHolderActionListener(this);
        binding.searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.poiRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.poiRecycler.setOnLoadMoreListener(this);

        mSearchAdapter = new ArrangePositionRecyclerAdapter();
        mPOIAdapter = new ArrangePositionRecyclerAdapter();

        mHeader = new ArrangePositionHeader(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int marginSE = (int) getResources().getDimension(R.dimen.general_padding_bit_larger);
        final int marginTB = (int) getResources().getDimension(R.dimen.general_padding);
        params.setMargins(marginSE, marginTB, marginSE, marginTB);
        mHeader.setLayoutParams(params);
        mPOIAdapter.addHeaderView(mHeader);

        binding.searchRecycler.setAdapter(mSearchAdapter);
        binding.poiRecycler.setAdapter(mPOIAdapter);

        requestPermission(PermissionConstants.LOCATION, RequestType.AUTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            setResult(SELECT_POSITION, data);
    }

    @Override
    protected void locationGranted() {
        startLocation();
    }

    @Override
    protected void locationDenied() {
        showPlaceHolder(StatePlaceHolder.TYPE_DENIED);
    }

    /**
     * 定位
     */
    private void startLocation() {
        showPlaceHolder(StatePlaceHolder.TYPE_LOADING);
        if (mClient == null) {
            mClient = new AMapLocationClient(this);
            mClient.setLocationListener(this);
        }
        if (mOption == null)
            mOption = new AMapLocationClientOption();

        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mOption.setOnceLocation(true);
        mOption.setLocationCacheEnable(true);
        mClient.setLocationOption(mOption);
        mClient.startLocation();
    }

    private void showPOI() {
        binding.placeholder.setVisibility(View.GONE);
        binding.searchRecycler.setVisibility(View.GONE);
        binding.poiRecycler.setVisibility(View.VISIBLE);
    }

    private void showSearch() {
        binding.placeholder.setVisibility(View.GONE);
        binding.searchRecycler.setVisibility(View.VISIBLE);
        binding.poiRecycler.setVisibility(View.GONE);
    }

    private void showPlaceHolder(int type) {
        binding.poiRecycler.setVisibility(View.GONE);
        binding.searchRecycler.setVisibility(View.GONE);
        binding.placeholder.setPlaceHolderType(type);
        binding.placeholder.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == 1000)
            mPOIAdapter.setList(poiResult.getPois());
        else showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            mAmapLocation = aMapLocation;
            if (aMapLocation.getErrorCode() == 0) {
                PoiSearch poiSearch = new PoiSearch(this, null);
                poiSearch.setOnPoiSearchListener(this);
                poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(mAmapLocation.getLatitude(), mAmapLocation.getLongitude()), 1000));
                poiSearch.searchPOIAsyn();
            } else showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
        } else showPlaceHolder(StatePlaceHolder.TYPE_ERROR);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mClient != null) {
            mClient.unRegisterLocationListener(this);
            mClient.onDestroy();
            mClient = null;
            mOption = null;
        }
    }

    @Override
    public void onPlaceHolderRetry(View view) {
        requestPermission(PermissionConstants.LOCATION, RequestType.MANUAL);
    }

    @Override
    public void autoLoadMore(EndlessRecyclerView recycler) {

    }
}