package com.example.schoolairdroprefactoredition;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mob.MobSDK;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        initAdapt();
        initMobPush();
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

    private void initMobPush() {
        MobSDK.init(this);
    }
}
