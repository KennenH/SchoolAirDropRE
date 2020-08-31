package com.example.schoolairdroprefactoredition.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.LocalServerSocket;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.example.schoolairdroprefactoredition.R;
import com.lxj.xpopup.XPopup;

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
        int coarsePermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (finalPermission != PERMISSION_GRANTED || coarsePermission != PERMISSION_GRANTED) {
            requestPermissions(new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, request);
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
        int writePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (readPermission != PERMISSION_GRANTED || writePermission != PERMISSION_GRANTED) {
            requestPermissions(new String[]
                    {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, request);
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
     * 弹出消息框引导用户手动设置
     * 只能接受{@link Automatically}
     *
     * @param res         弹窗标题
     * @param requestCode 要请求的权限{@link Automatically}请求码
     */
    private void popUpForPermission(@StringRes int res, int requestCode) {
        new XPopup.Builder(this).asConfirm(getString(res), null, getString(R.string.notNow), getString(R.string.goSetting), () -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, requestCode);
        }, null, false).show();
    }

    /**
     * 手动点击重试获取权限时，若此时没有权限并且之前已勾选不再提示，则会引导用户去设置中手动设置
     * 否则，系统弹窗提示获取权限
     * 此方法只能接受{@link Automatically}，否则会在用户拒绝权限时无限重复提示
     *
     * @param p1          权限1
     * @param p2          权限2
     * @param title       标题
     * @param requestCode 自动权限请求码
     */
    private void requestPermissionWithNoMoreRemainder(String p1, String p2, @StringRes int title, @Automatically int requestCode) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                p1) && !ActivityCompat.shouldShowRequestPermissionRationale(this, p2)) {
            popUpForPermission(title, requestCode);
        }
    }

    /**
     * 手动点击重试获取权限时，若此时没有权限并且之前已勾选不再提示，则会引导用户去设置中手动设置
     * 否则，系统弹窗提示获取权限
     * 此方法只能接受{@link Automatically}，否则会在用户拒绝权限时无限重复提示
     *
     * @param p           权限
     * @param title       标题
     * @param requestCode 自动权限请求码
     */
    private void requestPermissionWithNoMoreRemainder(String p, @StringRes int title, @Automatically int requestCode) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, p))
            popUpForPermission(title, requestCode);
    }

    // 到这里结果返回时系统弹窗已经弹过，因此若没有权限则无需在此检查
    // 但若之前用户勾选了不再提醒，则需要弹出自己的对话框引导用户手动给予权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("呵呵呵呵", "权限获取结果回调");
        // 自动定位权限
        if (requestCode == Automatically.LOCATION) {
            if (grantResults.length == 2
                    && grantResults[0] == PERMISSION_GRANTED
                    && grantResults[1] == PERMISSION_GRANTED)
                locationGranted();
            else
                locationDenied();
        }
        // 手动定位权限
        else if (requestCode == Manually.LOCATION) {
            if (grantResults.length == 2
                    && grantResults[0] == PERMISSION_GRANTED
                    && grantResults[1] == PERMISSION_GRANTED) {
                locationGranted();
            } else {
                locationDenied();
                requestPermissionWithNoMoreRemainder(Manifest.permission.CAMERA, R.string.cameraPermissionTitle, Automatically.LOCATION);
            }
        }
        // 自动相机权限
        else if (requestCode == Automatically.CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PERMISSION_GRANTED)
                cameraGranted();
            else
                cameraDenied();
        }
        // 手动相机权限
        else if (requestCode == Manually.CAMERA) {
            if (grantResults.length == 1
                    && grantResults[0] == PERMISSION_GRANTED)
                cameraGranted();
            else {
                cameraDenied();
                requestPermissionWithNoMoreRemainder(Manifest.permission.CAMERA, R.string.cameraPermissionTitle, Automatically.CAMERA);
            }
        }
        // 自动相册权限
        else if (requestCode == Automatically.ALBUM) {
            if (grantResults.length == 2
                    && grantResults[0] == PERMISSION_GRANTED
                    && grantResults[1] == PERMISSION_GRANTED) {

                // 有权限
                albumGranted();
            } else
                // 没权限
                albumDenied();
        }
        // 手动相机权限
        else if (requestCode == Manually.ALBUM) {
            if (grantResults.length == 2
                    && grantResults[0] == PERMISSION_GRANTED
                    && grantResults[1] == PERMISSION_GRANTED) {
                albumGranted();
            } else {
                albumDenied();
                requestPermissionWithNoMoreRemainder(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, R.string.locationPermissionTitle, Automatically.ALBUM);
            }
        }
    }
}