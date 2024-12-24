package com.xc.apex.nre.posdemo.view.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xc.apex.nre.posdemo.R;
import com.xc.apex.nre.posdemo.utils.ToastUtil;

public class BaseActivity extends AppCompatActivity {

    private static final long DOUBLE_BACK_PRESS_INTERVAL = 2000;
    private long backPressedTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + DOUBLE_BACK_PRESS_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            ToastUtil.showToast(this, getString(R.string.txt_exit_app_tips));
            backPressedTime = System.currentTimeMillis();
        }
    }
}
