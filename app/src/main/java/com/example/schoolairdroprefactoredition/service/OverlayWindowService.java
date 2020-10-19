package com.example.schoolairdroprefactoredition.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.schoolairdroprefactoredition.service.interfaces.IOverlayWindowCommunication;

public class OverlayWindowService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new OverlayWindowBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.END | Gravity.TOP;
        params.setTitle("Load Average");
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//        wm.addView(mView, params);
    }

    private class OverlayWindowBinder extends Binder implements IOverlayWindowCommunication {
        @Override
        public void onQuotePushReceived() {
            Toast.makeText(OverlayWindowService.this, "onQuotePushReceived", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemReviewPushReceived() {
            Toast.makeText(OverlayWindowService.this, "onItemReviewPushReceived", Toast.LENGTH_SHORT).show();
        }
    }
}
