package com.xc.apex.nre.posdemo;

import android.app.Application;
import android.content.Context;

import com.xc.apex.nre.posdemo.usb.UsbCommunicateManager;

public class BaseApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // 注册USB通讯回调
        UsbCommunicateManager.getInstance().receiveUsbCommunicateListener();
    }

    public static Context getContext() {
        return context;
    }
}
