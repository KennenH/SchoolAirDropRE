package com.example.schoolairdroprefactoredition.activity.addnew;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.PermissionBaseActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.HorizontalImageRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.SellingOption;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.example.schoolairdroprefactoredition.utils.decoration.HorizontalItemMarginDecoration;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SellingAddNewActivity extends PermissionBaseActivity implements View.OnClickListener, AMapLocationListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, SellingAddNewActivity.class);
        context.startActivity(intent);
        if (context instanceof AppCompatActivity)
            ((AppCompatActivity) context).overridePendingTransition(R.anim.enter_y_fragment, R.anim.alpha_out);
    }

    public static void startForResult(Context context) {
        Intent intent = new Intent(context, SellingAddNewActivity.class);
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.startActivityForResult(intent, REQUEST_ADD_NEW);
            activity.overridePendingTransition(R.anim.enter_y_fragment, R.anim.alpha_out);
        }
    }

    public static final int REQUEST_ADD_NEW = 888;// 请求码 表单提交
    public static final String ADD_KEY = "AddNew";// 键 表单提交

    public static final int REQUEST_CODE_COVER = 219;// 请求码 封面
    public static final int REQUEST_CODE_PIC_SET = 11;// 请求码 图片集

    private SimpleDraweeView mCover;
    private RecyclerView mPicRecycler;
    private SellingOption mTitle;
    private SellingOption mPrice;
    private SellingOption mLocation;
    private SellingOption isNegotiable;
    private SellingOption isSecondHand;
    private SellingOption mDescription;

    private AMapLocationClient mClient;
    private AMapLocationClientOption mOption;

    private List<LocalMedia> selected = new ArrayList<>();
    private List<LocalMedia> cover = new ArrayList<>();

    private HorizontalImageRecyclerAdapter mAdapter;

    private int request;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_add_new);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar(findViewById(R.id.toolbar));

        mAdapter = new HorizontalImageRecyclerAdapter();

        mCover = findViewById(R.id.cover);
        mPicRecycler = findViewById(R.id.pic_set);
        mTitle = findViewById(R.id.option_title);
        mPrice = findViewById(R.id.option_price);
        mLocation = findViewById(R.id.option_location);
        isNegotiable = findViewById(R.id.option_negotiable);
        isSecondHand = findViewById(R.id.option_secondHand);
        mDescription = findViewById(R.id.option_description);

        mCover.setOnClickListener(this);
        mPicRecycler.setOnClickListener(this);
        mTitle.setOnClickListener(this);
        mPrice.setOnClickListener(this);
        mLocation.setOnClickListener(this);
        isNegotiable.setOnClickListener(this);
        isSecondHand.setOnClickListener(this);
        mDescription.setOnClickListener(this);

        mPicRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mPicRecycler.addItemDecoration(new HorizontalItemMarginDecoration(getResources().getDimension(R.dimen.general_padding_small)));
        ImageView add = new ImageView(this);
        add.setLayoutParams(new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.home_item_image), (int) getResources().getDimension(R.dimen.home_item_image)));
        add.setImageResource(R.drawable.bg_add_pic);
        add.setOnClickListener(v -> {
            request = REQUEST_CODE_PIC_SET;
            requestAlbumPermission(RequestType.MANUAL);
        });
        mAdapter.addFooterView(add);
        mPicRecycler.setAdapter(mAdapter);

        requestLocationPermission(RequestType.AUTO);
    }

    @Override
    protected void locationGranted() {
        super.locationGranted();
        startLocation();
    }

    @Override
    protected void locationDenied() {
        super.locationDenied();
        mLocation.setDescription(getString(R.string.clickToGrantLocationPermission));
    }

    @Override
    protected void cameraGranted() {
        super.cameraGranted();
    }

    @Override
    protected void cameraDenied() {
        super.cameraDenied();
    }

    @Override
    protected void albumGranted() {
        super.albumGranted();
        if (request == REQUEST_CODE_COVER)
            MyUtil.pickPhotoFromAlbum(this, REQUEST_CODE_COVER, cover, 1);
        else if (request == REQUEST_CODE_PIC_SET)
            MyUtil.pickPhotoFromAlbum(this, REQUEST_CODE_PIC_SET, selected, 6);
    }

    @Override
    protected void albumDenied() {
        super.albumDenied();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SellingAddSetActivity.RESULT_CODE) {
                if (data != null) {
                    int type = data.getIntExtra(SellingAddSetActivity.TYPE, SellingAddSetActivity.TYPE_TITLE);
                    if (type == SellingAddSetActivity.TYPE_TITLE)
                        mTitle.setText(data.getStringExtra(SellingAddSetActivity.RESULT));
                    else
                        mDescription.setText(data.getStringExtra(SellingAddSetActivity.RESULT));
                }
            } else if (requestCode == REQUEST_CODE_COVER) {
                if (data != null) {
                    this.cover = PictureSelector.obtainMultipleResult(data);
                    LocalMedia cover = this.cover.get(0);
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)
                        mCover.setImageURI(Uri.fromFile(new File(cover.getPath())));
                    else mCover.setImageURI(Uri.fromFile(new File(cover.getAndroidQToPath())));
                }
            } else if (requestCode == REQUEST_CODE_PIC_SET) {
                if (data != null) {
                    List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
                    selected = list;
                    mAdapter.setList(list);
                }
            }
        }
    }

    /**
     * 定位
     */
    private void startLocation() {
        if (mClient == null) {
            mClient = new AMapLocationClient(this);
            mClient.setLocationListener(this);
        }
        if (mOption == null)
            mOption = new AMapLocationClientOption();
        mLocation.setDescription(getString(R.string.locating));
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mOption.setOnceLocation(true);
        mOption.setLocationCacheEnable(true);
        mClient.setLocationOption(mOption);
        mClient.startLocation();
    }

    /**
     * 提交物品表单
     */
    private void submit() {
        // if (submit success){
        // isSubmitted = true
        // finish
        //}

    }

    /**
     * 保存用户草稿
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    /**
     * 回复用户草稿
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.selling_add_done, menu);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(0, R.anim.popexit_y_fragment);
        } else if (id == R.id.add_submit) {
            submit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.popexit_y_fragment);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cover:
                request = REQUEST_CODE_COVER;
                requestAlbumPermission(RequestType.MANUAL);
                break;
            case R.id.option_title:
                SellingAddSetActivity.start(this, SellingAddSetActivity.TYPE_TITLE, mTitle.getText().toString());
                break;
            case R.id.option_price:

                break;
            case R.id.option_location:
                requestLocationPermission(RequestType.MANUAL);
                break;
            case R.id.option_negotiable:
                isNegotiable.toggle();
                break;
            case R.id.option_secondHand:
                isSecondHand.toggle();
                break;
            case R.id.option_description:
                SellingAddSetActivity.start(this, SellingAddSetActivity.TYPE_DESCRIPTION, mDescription.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                StringBuilder location = new StringBuilder();
                mLocation.setDescription(location
                        .append(aMapLocation.getProvince())
                        .append(aMapLocation.getCity())
                        .append(aMapLocation.getDistrict())
                        .append(aMapLocation.getStreet())
                        .append(aMapLocation.getStreetNum())
                        .toString());
            }
        }
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
}
