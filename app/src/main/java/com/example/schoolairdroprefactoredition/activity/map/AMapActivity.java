package com.example.schoolairdroprefactoredition.activity.map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.ImmersionStatusBarActivity;
import com.jaeger.library.StatusBarUtil;

import org.jetbrains.annotations.NotNull;

public class AMapActivity extends ImmersionStatusBarActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, AMapActivity.class);
        context.startActivity(intent);
    }

    private MapView mMapView;
    private AMap mMap;

    private MyLocationStyle mLocationStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        StatusBarUtil.setTranslucentForImageView(this, 40, mToolbar);

        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        displayMap();
    }

    private void displayMap() {
        if (mMap == null)
            mMap = mMapView.getMap();

        if (mLocationStyle == null)
            mLocationStyle = new MyLocationStyle();

        mLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);

        mMap.setMyLocationStyle(mLocationStyle);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
