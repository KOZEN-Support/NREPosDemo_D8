package com.xc.apex.nre.posdemo.usb;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.xc.apex.nre.lib_usb.CommunicateManager;
import com.xc.apex.nre.lib_usb.IReceiveListener;
import com.xc.apex.nre.posdemo.BaseApplication;
import com.xc.apex.nre.posdemo.R;
import com.xc.apex.nre.posdemo.model.PayCompleted;
import com.xc.apex.nre.posdemo.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UsbCommunicateManager implements IReceiveListener {
    private static final String TAG = "UsbCommunicateManager";

    private static final int USB_DISCONNECTED = -1;
    private static final int USB_CONNECTING = 0;
    private static final int USB_CONNECTED = 1;

    private static volatile UsbCommunicateManager instance;
    private Handler uiHandler;
    // 定时线程池：查询副屏连接状态
    private ScheduledThreadPoolExecutor connectStateExecutor;

    public static UsbCommunicateManager getInstance() {
        if (instance == null) {
            synchronized (UsbCommunicateManager.class) {
                if (instance == null) {
                    instance = new UsbCommunicateManager();
                }
            }
        }
        return instance;
    }

    private UsbCommunicateManager() {
        uiHandler = new Handler(Looper.getMainLooper());
        connectStateExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
    }

    public void receiveUsbCommunicateListener() {
        Log.d(TAG, "receiveUsbCommunicateListener");
        CommunicateManager.getInstance(BaseApplication.getContext()).setReceiveListener(this::onReceiveData);
    }

    public boolean isUSBConnected() {
        int state = CommunicateManager.getInstance(BaseApplication.getContext()).getConnectionState();
        return state == 1;
    }

    public int getUsbConnectedState() {
        return CommunicateManager.getInstance(BaseApplication.getContext()).getConnectionState();
    }

    public void sendCommandToHost(String command) {
        Log.d(TAG, "sendCommandToHost:: command = " + command);
        if (isUSBConnected()) {
            CommunicateManager.getInstance(BaseApplication.getContext()).sendData(command.getBytes());
        } else {
            showToast(BaseApplication.getContext().getResources().getString(R.string.txt_disconnected) + getUsbConnectedState() + ".");
        }
    }

    public void sendCommand2LaunchCustomerApk() {
        if (!isUSBConnected()) {
            Runnable task = () -> {
                Log.d(TAG, "Enable scheduled USB status query task");
                if (isUSBConnected()) {
                    Log.d(TAG, "The secondary screen connection is successful, ending the task");
                    connectStateExecutor.shutdown();
                    CommunicateManager.getInstance(BaseApplication.getContext()).sendData(SendCommand.START_CUSTOMER_APK.getBytes());
                    Log.d(TAG, "Send the command to start the secondary screen apk");
                }
            };
            connectStateExecutor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        } else {
            CommunicateManager.getInstance(BaseApplication.getContext()).sendData(SendCommand.START_CUSTOMER_APK.getBytes());
            Log.d(TAG, "Send the command to start the secondary screen apk");
        }
    }

    @Override
    public void onReceiveData(byte[] data) {
        Log.e(TAG, "onReceiveData");
        if (data != null && data.length > 0) {
            handleCommand(new String(data));
        }
    }

    private void handleCommand(String command) {
        Log.e(TAG, "handleCommand:: command = " + command);
        usbCallbackVisualization(command);

        if (command.equalsIgnoreCase(ReceiverCommand.PAY_SUCCESS)) {
            EventBus.getDefault().post(new PayCompleted(true));
        } else if (command.equalsIgnoreCase(ReceiverCommand.PAY_FAILED)) {
            EventBus.getDefault().post(new PayCompleted(false));
        }
    }

    private void showToast(String msg) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(BaseApplication.getContext(), msg);
            }
        });
    }

    // TODO 去掉toast指令提示
    private void usbCallbackVisualization(String data) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(BaseApplication.getContext(), "C-R = " + data);
            }
        });
    }
}
