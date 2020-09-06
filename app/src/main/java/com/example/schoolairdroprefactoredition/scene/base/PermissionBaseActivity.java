package com.example.schoolairdroprefactoredition.scene.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.example.schoolairdroprefactoredition.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.ConfirmPopupView;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PermissionBaseActivity extends AppCompatActivity {
    /**
     * 检查是否拥有定位权限
     *
     * @param type 请求类型 one of {@link RequestType#AUTO} {@link RequestType#MANUAL}
     */
    public void requestLocationPermission(@RequestType int type) {
        int request = type == RequestType.AUTO ? Automatically.LOCATION : Manually.LOCATION;
        int finalPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
//        int coarsePermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (finalPermission != PERMISSION_GRANTED
//                || coarsePermission != PERMISSION_GRANTED
        ) {
            requestPermissions(new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION
//                            , Manifest.permission.ACCESS_COARSE_LOCATION
                    }, request);
        } else locationGranted();
    }

    /**
     * {@link Automatically}
     * 检查是否有相机权限
     *
     * @param type 请求类型 one of {@link RequestType#AUTO} {@link RequestType#MANUAL}
     */
    public void requestCameraPermission(@RequestType int type) {
        int request = type == RequestType.AUTO ? Automatically.LOCATION : Manually.LOCATION;
        int permission = checkSelfPermission(Manifest.permission.CAMERA);
        if (permission != PERMISSION_GRANTED) {
            requestPermissions(new String[]
                    {Manifest.permission.CAMERA}, request);
        } else cameraGranted();
    }

    /**
     * {@link Automatically}
     * 检查是否有相册权限
     *
     * @param type 请求类型 one of {@link RequestType#AUTO} {@link RequestType#MANUAL}
     */
    public void requestAlbumPermission(@RequestType int type) {
        int request = type == RequestType.AUTO ? Automatically.LOCATION : Manually.LOCATION;
        int readPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
//        int writePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (readPermission != PERMISSION_GRANTED
//                || writePermission != PERMISSION_GRANTED
        ) {
            requestPermissions(new String[]
                    {Manifest.permission.READ_EXTERNAL_STORAGE
//                            , Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, request);
        } else albumGranted();
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
    private @interface Automatically {
        int LOCATION = 999; // requestCode 自动定位权限请求码
        int CAMERA = 888; // requestCode 自动相机权限请求码
        int ALBUM = 777; // requestCode 自动相册权限请求码
    }

    /**
     * 手动请求码，在检测到权限被拒绝时会弹窗引导用户手动设置
     */
    private @interface Manually {
        int LOCATION = 99; // requestCode 手动定位权限请求码
        int CAMERA = 88; // requestCode 手动相机权限请求码
        int ALBUM = 77; // requestCode 手动相册权限请求码
    }

    private ConfirmPopupView confirm = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            // 设置返回时检查自动权限
            permissionSettingResult(requestCode);
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
     * 当用户在此前勾选了不再提示并且这一次手动设置之后返回的未知结果
     * 根据{@link Automatically}请求码确定该调用的方法
     *
     * @param requestCode {@link Automatically}权限请求码
     */
    protected void permissionSettingResult(@Automatically int requestCode) {
        switch (requestCode) {
            case Automatically.LOCATION:
                requestLocationPermission(RequestType.AUTO);
                break;
            case Automatically.CAMERA:
                requestCameraPermission(RequestType.AUTO);
                break;
            case Automatically.ALBUM:
                requestAlbumPermission(RequestType.AUTO);
                break;
            default:
                break;
        }
    }

    /**
     * 权限被拒但未勾选不再提醒
     * 弹出消息框提示重试
     * 只能接受{@link Automatically}
     */
    private void popUpForPermissionAutomatically(String p, @StringRes int content) {
        if (confirm == null)
            confirm = new XPopup.Builder(this).asConfirm(getString(R.string.permissionTitle), getString(content), getString(R.string.imSure), getString(R.string.retry), () -> {
                if (p.equals(Manifest.permission.ACCESS_FINE_LOCATION))
                    requestLocationPermission(RequestType.MANUAL);
                else if (p.equals(Manifest.permission.CAMERA))
                    requestCameraPermission(RequestType.MANUAL);
                else if (p.equals(Manifest.permission.READ_EXTERNAL_STORAGE))
                    requestAlbumPermission(RequestType.MANUAL);
            }, this::locationDenied, false);
        confirm.show();
    }

    /**
     * 权限被拒且不再提醒
     * 弹出消息框引导用户手动设置
     * 只能接受{@link Automatically}
     */
    private void popUpForPermissionManually(@StringRes int content, @Automatically int requestCode) {
        if (confirm == null)
            confirm = new XPopup.Builder(this).asConfirm(getString(R.string.permissionTitle), getString(content), getString(R.string.notNow), getString(R.string.goSetting), () -> {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, requestCode);
            }, this::locationDenied, false);

        confirm.show();
    }

    // 到这里结果返回时系统弹窗已经弹过，因此若没有权限则无需在此检查
    // 但若此时请求码为手动，说明之前用户拒绝过，则需要判断用户是否之前勾选了不再提醒
    // 若否，则直接请求权限
    // 若是，则弹出提示框引导用户手动给与权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // 定位权限
        if (requestCode == Automatically.LOCATION || requestCode == Manually.LOCATION) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PERMISSION_GRANTED) {
                    locationGranted();
                } else if (grantResults[0] == PERMISSION_DENIED) {
                    locationDenied();
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        popUpForPermissionAutomatically(Manifest.permission.ACCESS_FINE_LOCATION, R.string.locationPermissionContentWithoutCheck);
                    } else {
                        locationDenied();
                        popUpForPermissionManually(R.string.locationPermissionContentWithCheck, Automatically.LOCATION);
//                        AppUtils.exitApp();
//                        if (requestCode == Manually.LOCATION) {
//                        }
                    }
                }
            }
        }

        // 相机权限
        else if (requestCode == Automatically.CAMERA || requestCode == Manually.CAMERA) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PERMISSION_GRANTED) {
                    locationGranted();
                } else if (grantResults[0] == PERMISSION_DENIED) {
                    cameraDenied();
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        popUpForPermissionAutomatically(Manifest.permission.CAMERA, R.string.cameraPermissionContentWithoutCheck);
                    } else {
                        cameraDenied();
                        if (requestCode == Manually.CAMERA) {
                            popUpForPermissionManually(R.string.cameraPermissionContentWithCheck, Automatically.CAMERA);
                        }
                    }
                }
            }
        }

        // 相册权限
        else if (requestCode == Automatically.ALBUM || requestCode == Manually.ALBUM) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PERMISSION_GRANTED) {
                    albumDenied();
                } else if (grantResults[0] == PERMISSION_DENIED) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        popUpForPermissionAutomatically(Manifest.permission.READ_EXTERNAL_STORAGE, R.string.albumPermissionContentWithoutCheck);
                    } else {
                        albumDenied();
                        if (requestCode == Manually.ALBUM) {
                            popUpForPermissionManually(R.string.albumPermissionContentWithCheck, Automatically.ALBUM);
                        }
                    }
                }
            }
        }
    }
}