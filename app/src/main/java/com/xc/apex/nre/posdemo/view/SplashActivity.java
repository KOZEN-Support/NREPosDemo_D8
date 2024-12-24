package com.xc.apex.nre.posdemo.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.xc.apex.nre.posdemo.R;
import com.xc.apex.nre.posdemo.usb.UsbCommunicateManager;
import com.xc.apex.nre.posdemo.view.base.BaseActivity;

public class SplashActivity extends BaseActivity {
    private static final long SPLASH_DELAY = 5000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 发送指令调起副屏APK
        UsbCommunicateManager.getInstance().sendCommand2LaunchCustomerApk();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainTabActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);
    }
}
