package com.example.schoolairdroprefactoredition;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;

public class Application extends android.app.Application {

    public static final String MIPUSH_APP_ID = "2882303761518719324";
    public static final String MIPUSH_APP_KEY = "5561871983324";

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        initAdapt();
        initMiPush();
    }

    private void initAdapt() {
        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig config = AutoSizeConfig.getInstance()
                .setLog(true)
                .setDesignWidthInDp(360)
                .setDesignHeightInDp(640)
                .setUseDeviceSize(true);
        float screenWidth = config.getScreenWidth();
        float screenHeight = config.getScreenHeight();
        float designWidthInDp = config.getDesignWidthInDp();
        float designHeightInDp = config.getDesignHeightInDp();
        float rateWidth = screenWidth / designWidthInDp;
        float rateHeight = screenHeight / designHeightInDp;
        if (rateWidth > rateHeight) {
            config.setBaseOnWidth(false);
        } else {
            config.setBaseOnWidth(true);
        }
    }

    private void initMiPush() {
        if (shouldInit()) {
            MiPushClient.registerPush(this, MIPUSH_APP_ID, MIPUSH_APP_KEY);
        }
        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                LogUtils.d(content, t);
            }

            @Override
            public void log(String content) {
                LogUtils.d(content);
            }
        };
        Logger.setLogger(this, newLogger);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getApplicationInfo().processName;
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

}
