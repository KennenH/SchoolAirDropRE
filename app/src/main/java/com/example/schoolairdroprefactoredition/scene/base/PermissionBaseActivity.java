package com.example.schoolairdroprefactoredition.scene.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.utils.AppConfig;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DialogUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.mob.MobSDK;
import com.mob.OperationCallback;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PermissionBaseActivity extends ImmersionStatusBarActivity {

    /**
     * 请求获取权限
     * 在执行需要权限的方法之前必须先执行此方法来判断是否有权限
     * 在 XXGranted 方法(eg. {@link PermissionBaseActivity#locationGranted()})中执行需要权限的操作
     * 在 XXDenied 方法(eg. {@link PermissionBaseActivity#locationDenied()})中执行权限拒绝后的操作
     * <p>
     *
     * @param type one of {@link RequestType#MANUAL} or {@link RequestType#AUTO}
     *             在类型为 {@link RequestType#MANUAL} 时若已勾选不再提醒，则弹窗引导用户手动设置 **适用于用户手动重试时**
     *             反之若类型为 {@link RequestType#AUTO} 时若权限被拒绝则不会询问 **适用于页面打开时自动检查权限**
     */
    public void requestPermission(@PermissionConstants.Permission String permission, @RequestType int type) {
        int res = 0;
        switch (permission) {
            case PermissionConstants.LOCATION:
                res = R.string.locationPermissionContent;
                break;
            case PermissionConstants.CAMERA:
                res = R.string.cameraPermissionContent;
                break;
            case PermissionConstants.STORAGE:
                res = R.string.albumPermissionContent;
                break;
            case PermissionConstants.PHONE:
                res = R.string.phonePermissionContent;
                break;
            default:
                break;
        }

        final int finalRes = res;
        PermissionUtils.permission(permission)
                .callback((isAllGranted, granted, deniedForever, denied) -> {
                    // 权限允许
                    if (isAllGranted) {
                        switch (permission) {
                            case PermissionConstants.LOCATION:
                                locationGranted();
                                break;
                            case PermissionConstants.CAMERA:
                                cameraGranted();
                                break;
                            case PermissionConstants.STORAGE:
                                albumGranted();
                                break;
                            case PermissionConstants.PHONE:
                                phoneGranted();
                                break;
                            default:
                                break;
                        }
                        return;
                    }

                    if (deniedForever.size() != 0 && type == RequestType.MANUAL) {// 拒绝并不再提醒，但是又手动做需要权限的操作
                        popUpToSettingsForPermission(finalRes, permission);
                    } else if (denied.size() != 0) { // 拒绝但未勾选不再提醒
                        popUpForRequestPermission(finalRes, permission);
                    } else {
                        LogUtils.d("权限请求失败了，将调用父类权限拒绝方法");
                    }

                    switch (permission) {
                        case PermissionConstants.LOCATION:
                            locationDenied();
                            break;
                        case PermissionConstants.CAMERA:
                            cameraDenied();
                            break;
                        case PermissionConstants.STORAGE:
                            albumDenied();
                            break;
                        case PermissionConstants.PHONE:
                            phoneDenied();
                            break;
                        default:
                            break;
                    }
                })
                .request();
    }

    /**
     * 检查是否已经同意服务条款和隐私政策 仅用于{@link com.example.schoolairdroprefactoredition.scene.main.MainActivity}
     * 若已同意则在{@link PermissionBaseActivity#initAppMainAfterAgreeToTermsOfService()}中启动主页面的渲染
     * 否则退出App
     */
    public void checkIfAgreeToTermsOfServiceAndPrivacyPolicy(Context context) {
        if (!(context instanceof MainActivity)) {
            throw new IllegalStateException("checkIfAgreeToTermsOfServiceAndPrivacyPolicy can only be called in MainActivity.");
        }

        boolean isAgreed = getSharedPreferences(ConstantUtil.START_UP_PREFERENCE, MODE_PRIVATE).getBoolean(ConstantUtil.START_UP_IS_TERMS_AGREED, false);
        if (!isAgreed) {
            new XPopup.Builder(this)
                    .isClickThrough(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(new BasePopupView(this) {
                        @Override
                        protected void init() {
                            super.init();
                            TextView agree = findViewById(R.id.dialog_privacy_agree);
                            TextView disagree = findViewById(R.id.dialog_privacy_disagree);
                            ((CheckBox) findViewById(R.id.dialog_privacy_check)).setOnCheckedChangeListener((button, selected) -> {
                                agree.setEnabled(selected);
                                if (selected)
                                    agree.setText(getString(R.string.agreeAboveAgreementAndStartToUse));
                                else
                                    agree.setText(getString(R.string.pleaseAgreeAboveTermsFirst));
                            });

                            agree.setOnClickListener(v -> handleAgreementToTermsOfService(this));
                            disagree.setOnClickListener(v -> AppUtils.exitApp());
                        }

                        @Override
                        protected int getPopupLayoutId() {
                            return R.layout.dialog_privacy_policy;
                        }
                    }).show();
        } else {
            initAppMainAfterAgreeToTermsOfService();
        }
    }

    /**
     * 仅检查是否有某个权限，不请求权限，即权限被拒绝时保持完全沉默
     * 需要权限的操作仍旧在 granted 方法和 denied 方法中执行
     */
    public void checkPermissionWithoutRequest(int requestCode) {
        switch (requestCode) {
            case Automatically.LOCATION:
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                    locationGranted();
                } else {
                    locationDenied();
                }
                break;
            case Automatically.CAMERA:
                if (checkSelfPermission(Manifest.permission.CAMERA) == PERMISSION_GRANTED) {
                    cameraGranted();
                } else {
                    cameraDenied();
                }
                break;
            case Automatically.ALBUM:
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
                    albumGranted();
                } else {
                    albumDenied();
                }
                break;
            case Automatically.PHONE:
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PERMISSION_GRANTED) {
                    phoneGranted();
                } else {
                    phoneDenied();
                }
            default:
                break;
        }
    }

    /**
     * 请求码类型
     * {@link Automatically} 自动
     * {@link Manually} 手动
     */
    public @interface RequestType {
        int AUTO = 100;
        int MANUAL = 200;
    }

    /**
     * 自动请求码，在检测到权限被拒绝时保持沉默
     */
    public @interface Automatically {
        int LOCATION = 999;
        int CAMERA = 888;
        int ALBUM = 777;
        int PHONE = 666;
    }

    /**
     * 手动请求码，在检测到权限被拒绝时会弹窗引导用户手动设置
     */
    public @interface Manually {
    }

    /**
     * 处理用户同意后请求
     */
    private void handleAgreementToTermsOfService(BasePopupView dialog) {
        if (AppConfig.IS_DEBUG) {
            dialog.dismiss();
            getSharedPreferences(ConstantUtil.START_UP_PREFERENCE, MODE_PRIVATE)
                    .edit()
                    .putBoolean(ConstantUtil.START_UP_IS_TERMS_AGREED, false)
                    .apply();
            initAppMainAfterAgreeToTermsOfService();
        } else {
            LoadingPopupView loading = MyUtil.loading(this);
            loading.show();
            MobSDK.submitPolicyGrantResult(true, new OperationCallback<Void>() {
                @Override
                public void onComplete(Void aVoid) {
                    getSharedPreferences(ConstantUtil.START_UP_PREFERENCE, MODE_PRIVATE)
                            .edit()
                            .putBoolean(ConstantUtil.START_UP_IS_TERMS_AGREED, true)
                            .apply();

                    loading.dismissWith(dialog::dismiss);
                    initAppMainAfterAgreeToTermsOfService();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    loading.dismissWith(() ->
                            DialogUtil.showCenterDialog(PermissionBaseActivity.this, DialogUtil.DIALOG_TYPE.FAILED, R.string.termsOfServiceAgreeFailed));
                }
            });
        }
    }

    /**
     * 服务条款和隐私政策同意成功
     * 开始初始化App
     */
    protected void initAppMainAfterAgreeToTermsOfService() {
    }

    /**
     * 定位权限获取
     */
    protected void locationGranted() {
    }

    /**
     * 定位权限拒绝
     */
    protected void locationDenied() {
    }

    /**
     * 相机权限获取
     */
    protected void cameraGranted() {
    }

    /**
     * 相机权限拒绝
     */
    protected void cameraDenied() {
    }

    /**
     * 相册权限获取
     */
    protected void albumGranted() {
    }

    /**
     * 相册权限拒绝
     */
    protected void albumDenied() {
    }

    /**
     * 电话权限获取
     */
    protected void phoneGranted() {
    }

    /**
     * 电话权限拒绝
     */
    protected void phoneDenied() {
    }

    /**
     * 用户之前拒绝了权限，但是并没有勾选不再提示，此时需要弹窗提示用户授予权限
     *
     * @param res        弹窗提示语
     * @param permission 权限串{@link PermissionConstants.Permission}
     */
    private void popUpForRequestPermission(@StringRes int res, @PermissionConstants.Permission String permission) {
        int request = 0;
        switch (permission) {
            case PermissionConstants.LOCATION:
                request = Automatically.LOCATION;
                break;
            case PermissionConstants.CAMERA:
                request = Automatically.CAMERA;
                break;
            case PermissionConstants.STORAGE:
                request = Automatically.ALBUM;
                break;
            case PermissionConstants.PHONE:
                request = Automatically.PHONE;
                break;
            default:
                break;
        }
        int finalRequest = request;
        new XPopup.Builder(this)
                .isDarkTheme(isDarkTheme)
                .asConfirm(getString(R.string.permissionTitle), getString(res), getString(android.R.string.cancel), getString(android.R.string.ok)
                        , () -> {
                            switch (permission) {
                                case PermissionConstants.LOCATION:
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, finalRequest);
                                    break;
                                case PermissionConstants.CAMERA:
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, finalRequest);
                                    break;
                                case PermissionConstants.STORAGE:
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, finalRequest);
                                    break;
                                case PermissionConstants.PHONE:
                                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, finalRequest);
                                    break;
                                default:
                                    break;
                            }
                        }
                        , () -> {
                            switch (permission) {
                                case PermissionConstants.LOCATION:
                                    locationDenied();
                                    break;
                                case PermissionConstants.CAMERA:
                                    cameraDenied();
                                    break;
                                case PermissionConstants.STORAGE:
                                    albumDenied();
                                    break;
                                case PermissionConstants.PHONE:
                                    phoneDenied();
                                    break;
                                default:
                                    break;
                            }
                        }, false).show();
    }

    /**
     * 用户之前拒绝了权限并勾选不再提示，但是现在又手动点击需要权限的操作，此时需要弹出对话框来引导用户
     * 去系统设置中手动设置
     *
     * @param res        对话框提示res
     * @param permission 权限{@link PermissionConstants.Permission}
     */
    private void popUpToSettingsForPermission(@StringRes int res, @PermissionConstants.Permission String permission) {
        new XPopup.Builder(this)
                .isDarkTheme(isDarkTheme)
                .asConfirm(getString(R.string.permissionTitle), getString(res), getString(android.R.string.cancel), getString(android.R.string.ok)
                        , () -> { // 点击确定，引导至系统设置
                            switch (permission) {
                                case PermissionConstants.LOCATION:
                                    goSettings(Automatically.LOCATION);
                                    break;
                                case PermissionConstants.CAMERA:
                                    goSettings(Automatically.CAMERA);
                                    break;
                                case PermissionConstants.STORAGE:
                                    goSettings(Automatically.ALBUM);
                                    break;
                                case PermissionConstants.PHONE:
                                    goSettings(Automatically.PHONE);
                                    break;
                                default:
                                    break;
                            }
                        }
                        , () -> { // 点击取消，do nothing
                        },
                        false).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 从系统设置返回至App时检查自动权限，若未允许也不再申请
        checkPermissionWithoutRequest(requestCode);
    }

    /**
     * 进入设置，手动给予权限
     *
     * @param requestCode 必须是自动权限{@link Automatically}，该requestCode将会传递至
     *                    {@link #onActivityResult(int, int, Intent)}中，从而传递至
     *                    {@link #checkPermissionWithoutRequest(int)}，该方法只接收自动权限
     */
    private void goSettings(int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, requestCode);
    }
}