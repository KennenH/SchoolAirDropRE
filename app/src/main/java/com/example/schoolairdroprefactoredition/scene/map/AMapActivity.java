package com.example.schoolairdroprefactoredition.scene.map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.BarUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ActivityAmapBinding;
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity;
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil;

import org.jetbrains.annotations.NotNull;

public class AMapActivity extends PermissionBaseActivity implements LocationSource, AMapLocationListener, View.OnClickListener {

    public static final int REQUEST_CODE = 300;
    public static final String LOCATION_KEY = "AMapLocation";

    private AMapLocationClient mClient;
    private AMapLocationClientOption mOption;
    private AMapLocation mLocation;

    private AMap mMap;

    private ActivityAmapBinding binding;

    private MyLocationStyle mLocationStyle;

    private OnLocationChangedListener mOnLocationChangedListener;

    public static void startForResult(Context context) {
        Intent intent = new Intent(context, AMapActivity.class);
        ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAmapBinding.inflate(getLayoutInflater());
        setContentView(binding.root);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        StatusBarUtil.setTranslucentForImageView(this, binding.toolbar);
        BarUtils.setNavBarColor(this, getColor(R.color.darkTranslucent));
        BarUtils.setNavBarLightMode(this, false);
        setSupportActionBar(binding.toolbar);

        binding.mapLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(""))
                    binding.mapLocation.setVisibility(View.GONE);
                else
                    binding.mapLocation.setVisibility(View.VISIBLE);
            }
        });
        binding.mapLocation.setOnClickListener(this);
        binding.mapRelocate.setOnClickListener(this);
        binding.relocating.setOnClickListener(this);
        binding.map.onCreate(savedInstanceState);

        displayMap();

        locating();
        requestPermission(PermissionConstants.LOCATION, RequestType.AUTO);
    }

    @Override
    protected void setActivityTheme() {
        // do nothing
    }

    /**
     * 正在定位
     */
    private void locating() {
        binding.mapRelocate.setVisibility(View.INVISIBLE);
        binding.relocating.setVisibility(View.VISIBLE);
        binding.mapLocation.setText(getString(R.string.locating));
    }

    /**
     * 定位完成
     */
    private void locateDone() {
        binding.mapRelocate.setVisibility(View.VISIBLE);
        binding.relocating.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.map_relocate) {
            locating();
            requestPermission(PermissionConstants.LOCATION, RequestType.MANUAL);
        }
    }

    @Override
    protected void locationGranted() {
        super.locationGranted();
        displayMap();
    }

    @Override
    protected void locationDenied() {
        super.locationDenied();
        binding.mapLocation.setText(getString(R.string.permissionDenied));
    }

    private void displayMap() {
        if (mMap == null)
            mMap = binding.map.getMap();

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
        locateDone();
        if (aMapLocation != null && mOnLocationChangedListener != null) {
            if (aMapLocation.getErrorCode() == 0) {
                binding.mapLocation.setText(aMapLocation.getAddress());
                mOnLocationChangedListener.onLocationChanged(aMapLocation);
                mLocation = aMapLocation;
//                Log.d("AMapActivity", aMapLocation.getAddress());
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(13f), 200, new AMap.CancelableCallback() {
//                    @Override
//                    public void onFinish() {
//                    }
//
//                    @Override
//                    public void onCancel() {
//                    }
//                });
            } else {
                binding.mapLocation.setText(getString(R.string.errorNetLocation));
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        locating();
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

        if (mClient != null) {
            mClient.stopLocation();
            mClient.onDestroy();
            mClient = null;
        }
        binding.map.onDestroy();
        if (mOnLocationChangedListener != null)
            mOnLocationChangedListener = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 重新绘制加载地图
        binding.map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 暂停地图的绘制
        binding.map.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存地图当前的状态
        binding.map.onSaveInstanceState(outState);
    }
}
