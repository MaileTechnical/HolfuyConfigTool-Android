package com.holfuy.configtool.usb

import android.hardware.usb.UsbDevice

object HolfuyUsb
{
    const val PRODUCT_ID = 0xA316

    fun isSupported(device: UsbDevice): Boolean
    {
        return device.productId == PRODUCT_ID
    }
}