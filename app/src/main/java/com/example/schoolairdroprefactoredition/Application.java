package com.example.schoolairdroprefactoredition;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import androidx.appcompat.app.AppCompatDelegate;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.cache.UserSettingsCache;
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
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
        initAppTheme();
    }

    private void initAppTheme() {
        JsonCacheUtil mJsonCacheUtil = JsonCacheUtil.newInstance();
        UserSettingsCache settings = mJsonCacheUtil.getValue(UserSettingsCache.USER_SETTINGS, UserSettingsCache.class);
        if (settings == null) settings = new UserSettingsCache(false);
        AppCompatDelegate.setDefaultNightMode(settings.isDarkTheme() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void setAppTheme(boolean isDarkTheme) {
        JsonCacheUtil mJsonCacheUtil = JsonCacheUtil.newInstance();
        UserSettingsCache settings = mJsonCacheUtil.getValue(UserSettingsCache.USER_SETTINGS, UserSettingsCache.class);
        if (settings == null) settings = new UserSettingsCache(false);
        AppCompatDelegate.setDefaultNightMode(isDarkTheme ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        settings.setDarkTheme(isDarkTheme);
        mJsonCacheUtil.saveCache(UserSettingsCache.USER_SETTINGS, settings);
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
        config.setBaseOnWidth(!(rateWidth > rateHeight));
    }

    private void initMiPush() {
        if (shouldInit()) {
            MiPushClient.registerPush(this, MIPUSH_APP_ID, MIPUSH_APP_KEY);
        }

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
//        Logger.setLogger(this, newLogger);
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