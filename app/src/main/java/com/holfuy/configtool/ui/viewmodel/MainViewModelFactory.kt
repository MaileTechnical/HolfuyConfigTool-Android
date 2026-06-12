package com.holfuy.configtool.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.holfuy.configtool.device.HolfuyDevice
import com.holfuy.configtool.usb.UsbDeviceProvider

class MainViewModelFactory(
    private val device: HolfuyDevice,
    private val usbDeviceProvider: UsbDeviceProvider
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(device, usbDeviceProvider) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}