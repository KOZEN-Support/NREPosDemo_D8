package com.xc.apex.nre.posdemo.usb;

/**
 * 接收副屏的指令合集。指令统一已‘C’开头
 */
public class ReceiverCommand {
    // 支付成功
    public static final String PAY_SUCCESS = "C000001";

    // 支付失败
    public static final String PAY_FAILED = "C000002";
}
