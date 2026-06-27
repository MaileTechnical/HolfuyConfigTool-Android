package com.holfuy.configtool.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.holfuy.configtool.device.HolfuyDevice
import com.holfuy.configtool.usb.UsbDeviceProvider

class MainViewModelFactory(
    private val holfuyDevice: HolfuyDevice,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T
    {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(
            holfuyDevice
        ) as T
    }
}