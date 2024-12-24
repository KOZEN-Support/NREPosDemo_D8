package com.xc.apex.nre.lib_usb;

import android.content.Context;
import android.xccommunication.IXcUsbListener;
import android.xccommunication.XcUsbManager;

public class CommunicateManager {
    private static final String TAG = "POS_CommunicateManager";

    private static volatile CommunicateManager instance;
    private XcUsbManager mXcUsbManager;
    private IReceiveListener mReceiveListener;

    public static CommunicateManager getInstance(Context context) {
        if (instance == null) {
            synchronized (CommunicateManager.class) {
                if (instance == null) {
                    instance = new CommunicateManager(context);
                }
            }
        }
        return instance;
    }

    private CommunicateManager(Context context) {
        mXcUsbManager = (XcUsbManager) context.getSystemService(Context.XC_USB_SERVICE);
    }

    public String getSubBuildNumber() {
        return mXcUsbManager.getSubBuildNumber();
    }

    public String getSubModel() {
        return mXcUsbManager.getSubModel();
    }

    public String getSubSn() {
        return mXcUsbManager.getSubSn();
    }

    public String getSubFwVersion() {
        return mXcUsbManager.getSubFwVersion();
    }

    public String getSubSpFwVersion() {
        return mXcUsbManager.getSubSpFwVersion();
    }

    public void subPowerOff() {
        mXcUsbManager.subPowerOff();
    }

    public void subPowerOn() {
        mXcUsbManager.subPowerOn();
    }

    public void subReboot() {
        mXcUsbManager.subReboot();
    }

    public void syncSubDateTime() {
        mXcUsbManager.setSubDateTime();
    }

    public boolean sendData(byte[] data) {
        try {
            return mXcUsbManager.sendData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getConnectionState() {
        try {
            return mXcUsbManager.getConnectionState();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void setReceiveListener(IReceiveListener listener) {
        mReceiveListener = listener;
        mXcUsbManager.setReceiveListener(new IXcUsbListener.Stub() {
            @Override
            public void onReceiveData(byte[] data) {
                mReceiveListener.onReceiveData(data);
            }
        });
    }

}
