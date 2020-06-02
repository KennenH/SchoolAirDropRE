package com.example.schoolairdroprefactoredition.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.components.SheetAlways;
import com.jaeger.library.StatusBarUtil;

import org.jetbrains.annotations.NotNull;

public class AMapActivity extends AppCompatActivity {

    private MapView mMapView;
    private AMap mMap;

    private MyLocationStyle mLocationStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mMapView = findViewById(R.id.map);

        StatusBarUtil.setTranslucentForImageView(this, 40, mToolbar);

        mMapView.onCreate(savedInstanceState);

        if (mMap == null)
            mMap = mMapView.getMap();

        if (mLocationStyle == null)
            mLocationStyle = new MyLocationStyle();

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_left_fill, getTheme()));
        }
        mLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        mMap.setMyLocationStyle(mLocationStyle);
        mMap.setMyLocationEnabled(true);
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
