package com.example.schoolairdroprefactoredition.scene.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.SheetAvatarBinding;
import com.example.schoolairdroprefactoredition.domain.DomainToken;
import com.example.schoolairdroprefactoredition.domain.base.DomainBaseUserInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DialogUtil;
import com.example.schoolairdroprefactoredition.utils.ImageUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.impl.LoadingPopupView;

import java.lang.reflect.InvocationTargetException;

import javadz.beanutils.BeanUtils;

import static com.example.schoolairdroprefactoredition.scene.user.UserActivity.REQUEST_UPDATE;

public class UserUpdateAvatarActivity extends AppCompatActivity implements BaseStateViewModel.OnRequestListener, View.OnLongClickListener, View.OnClickListener {

    /**
     * @param token 验证信息
     * @param info  我的用户信息
     */
    public static void start(Context context, DomainToken token, Object info) {
        if (token == null) return;

        DomainBaseUserInfo my = new DomainBaseUserInfo();
        try {
            BeanUtils.copyProperties(my, info);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(context, UserUpdateAvatarActivity.class);
        intent.putExtra(ConstantUtil.KEY_USER_INFO, my);
        intent.putExtra(ConstantUtil.KEY_TOKEN, token);
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_UPDATE);
        }
    }

    public static final int REQUEST_ALBUM = 99;
    public static final int REQUEST_CAMERA = 88;

    private BottomSheetDialog mDialog;
    private SimpleDraweeView mAvatar;
    private LoadingPopupView mLoading;

    private UserAvatarViewModel viewModel;

    private Bundle bundle;

    private DomainBaseUserInfo info;
    private DomainToken token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update_avatar);
        viewModel = new ViewModelProvider(this).get(UserAvatarViewModel.class);
        viewModel.setOnRequestListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StatusBarUtil.setTranslucentForImageView(this, toolbar);

        bundle = getIntent().getExtras();
        if (bundle == null)
            bundle = new Bundle();

        token = (DomainToken) bundle.getSerializable(ConstantUtil.KEY_TOKEN);
        info = (DomainBaseUserInfo) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);

        mAvatar = findViewById(R.id.avatar);
        mAvatar.setOnLongClickListener(this);

        init();
    }

    private void init() {
        try {
            ImageUtil.loadImage(mAvatar, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + info.getUser_img_path(), R.drawable.ic_logo_alpha_white);
        } catch (NullPointerException e) {
            LogUtils.d("info null");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.avatar) {
            showAvatarDialog();
            return true;
        }
        return false;
    }

    private void showAvatarDialog() {
        if (mDialog == null) {
            try {
                mDialog = new BottomSheetDialog(this);
                SheetAvatarBinding binding = SheetAvatarBinding.inflate(LayoutInflater.from(this));
                mDialog.setContentView(binding.getRoot());

                binding.takePhoto.setOnClickListener(this);
                binding.selectFromAlbum.setOnClickListener(this);
                binding.saveToAlbum.setOnClickListener(this);
                binding.cancel.setOnClickListener(this);

                View view1 = mDialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
                view1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.transparent, this.getTheme()));
                final BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(view1);
                bottomSheetBehavior.setSkipCollapsed(true);
                bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            mDialog.dismiss();
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    }
                });
            } catch (NullPointerException e) {
                LogUtils.d("dialog null");
            }
        }
        mDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) { // 使用相机修改头像回调
                if (data != null) {
                    LocalMedia photo = PictureSelector.obtainMultipleResult(data).get(0);

                    showLoading();
                    String qPath = photo.getAndroidQToPath();
                    viewModel.updateAvatar(token.getAccess_token(), qPath == null ? photo.getPath() : qPath).observe(this, bean -> {
                        updateAvatar(bean.getUser_img_path());
                    });
                }
            } else if (requestCode == REQUEST_ALBUM) { // 使用相册选择图片修改头像回调
                if (data != null) {
                    LocalMedia photo = PictureSelector.obtainMultipleResult(data).get(0);

                    showLoading();
                    String qPath = photo.getAndroidQToPath();
                    viewModel.updateAvatar(token.getAccess_token(), qPath == null ? photo.getPath() : qPath).observe(this, bean -> {
                        updateAvatar(bean.getUser_img_path());
                    });
                }
            }
        }
    }

    private void updateAvatar(String avatarUrl) {
        dismissLoading(() ->
                DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.SUCCESS, R.string.successAvatar));
        try {
            mAvatar.setImageURI(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + avatarUrl);
            info.setUser_img_path(avatarUrl);
            bundle.putSerializable(ConstantUtil.KEY_USER_INFO, info);
        } catch (NullPointerException e) {
            LogUtils.d(avatarUrl);
        }
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.take_photo:
                MyUtil.takePhoto(this, REQUEST_CAMERA, true);
                mDialog.dismiss();
                break;
            case R.id.select_from_album:
                MyUtil.pickPhotoFromAlbum(this, REQUEST_ALBUM, 1, true, true, false);
                mDialog.dismiss();
                break;
            case R.id.save_to_album:
                // todo 保存图片至相册
                DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.SUCCESS, R.string.successSave);
                mDialog.dismiss();
                break;
            case R.id.cancel:
                mDialog.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onError() {
        runOnUiThread(() -> dismissLoading(() ->
                DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed)
        ));
    }

    private void showLoading() {
        if (mLoading == null)
            mLoading = MyUtil.loading(this);

        mLoading.show();
    }

    private void dismissLoading(Runnable task) {
        if (mLoading != null)
            mLoading.dismissWith(task);
    }
}