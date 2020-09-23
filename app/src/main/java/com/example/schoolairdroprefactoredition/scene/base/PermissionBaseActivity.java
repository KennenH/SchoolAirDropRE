package com.example.schoolairdroprefactoredition.scene.base;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.example.schoolairdroprefactoredition.R;
import com.lxj.xpopup.XPopup;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PermissionBaseActivity extends ImmersionStatusBarActivity {

    /**
     * 请求获取权限
     * 在{@link RequestType#MANUAL}时若不再提醒，则弹窗引导用户手动设置
     * 否则不弹窗
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
            default:
                break;
        }

        int finalRes = res;
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
                            default:
                                break;
                        }
                        return;
                    }

                    if (deniedForever.size() != 0 && type == RequestType.MANUAL) {// 拒绝并不再提醒
                        popUpToSettingsForPermission(finalRes, permission);
                    } else if (denied.size() != 0) { // 拒绝但未勾选不再提醒
                        popUpForRequestPermission(finalRes, permission);
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
                        default:
                            break;
                    }
                })
                .request();
    }

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
            default:
                break;
        }
        int finalRequest = request;
        new XPopup.Builder(this).asConfirm(getString(R.string.permissionTitle), getString(res), getString(android.R.string.cancel), getString(android.R.string.ok)
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
                        default:
                            break;
                    }
                }, false).show();
    }

    private void popUpToSettingsForPermission(@StringRes int res, @PermissionConstants.Permission String permission) {
        new XPopup.Builder(this).asConfirm(getString(R.string.permissionTitle), getString(res), getString(android.R.string.cancel), getString(android.R.string.ok)
                , () -> {
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
                        default:
                            break;
                    }
                }
                , () -> { // do nothing
                },
                false).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 设置返回时检查自动权限
        checkPermissionWithoutRequest(requestCode);
    }

    /**
     * 进入设置，手动给予权限
     * 只接受{@link RequestType#AUTO}
     */
    private void goSettings(int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 仅检查是否有定位权限，不请求权限
     */
    public void checkPermissionWithoutRequest(int requestCode) {
        switch (requestCode) {
            case Automatically.LOCATION:
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED)
                    locationGranted();
                else locationDenied();
                break;
            case Automatically.CAMERA:
                if (checkSelfPermission(Manifest.permission.CAMERA) == PERMISSION_GRANTED)
                    cameraGranted();
                else cameraDenied();
                break;
            case Automatically.ALBUM:
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED)
                    albumGranted();
                else albumDenied();
                break;
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
     * 自动请求码，在检测到权限被拒绝时不再弹窗提示
     */
    public @interface Automatically {
        int LOCATION = 999; // requestCode 自动定位权限请求码
        int CAMERA = 888; // requestCode 自动相机权限请求码
        int ALBUM = 777; // requestCode 自动相册权限请求码
    }

    /**
     * 手动请求码，在检测到权限被拒绝时会弹窗引导用户手动设置
     */
    public @interface Manually {
        int LOCATION = 99; // requestCode 手动定位权限请求码
        int CAMERA = 88; // requestCode 手动相机权限请求码
        int ALBUM = 77; // requestCode 手动相册权限请求码
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
}