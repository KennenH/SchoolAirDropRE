package com.example.schoolairdroprefactoredition.activity.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.ImmersionStatusBarActivity;
import com.jaeger.library.StatusBarUtil;

import org.jetbrains.annotations.NotNull;

public class AMapActivity extends ImmersionStatusBarActivity implements LocationSource, AMapLocationListener {

    public static final int REQUEST_CODE = 300;
    public static final String LOCATION_KEY = "AMapLocation";

    private AMapLocationClient mClient;
    private AMapLocationClientOption mOption;
    private AMapLocation mLocation;

    private MapView mMapView;
    private AMap mMap;

    private MyLocationStyle mLocationStyle;

    private OnLocationChangedListener mOnLocationChangedListener;

    public static void start(Context context) {
        Intent intent = new Intent(context, AMapActivity.class);
        ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_CODE);
    }

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

        mMap.setLocationSource(this);
        mMap.setMyLocationStyle(mLocationStyle);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && mOnLocationChangedListener != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mOnLocationChangedListener.onLocationChanged(aMapLocation);
                mLocation = aMapLocation;
                Log.d("AMapActivity", aMapLocation.getAddress());
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(13f), 200, new AMap.CancelableCallback() {
//                    @Override
//                    public void onFinish() {
//
//                    }
//
//                    @Override
//                    public void onCancel() {
//                    }
//                });
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListener = onLocationChangedListener;
        if (mClient == null) mClient = new AMapLocationClient(this);
        if (mOption == null) mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mOption.setOnceLocation(true);
        mOption.setLocationCacheEnable(true);
        mClient.setLocationOption(mOption);
        mClient.setLocationListener(this);
        mClient.startLocation();
    }

    @Override
    public void deactivate() {
        if (mClient.isStarted())
            mClient.stopLocation();
        mClient = null;
        mOnLocationChangedListener = null;
    }

    private void sendBackLocation() {
        Intent intent = new Intent();
        intent.putExtra(LOCATION_KEY, mLocation);
        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            sendBackLocation();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        sendBackLocation();
        super.onBackPressed();
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
