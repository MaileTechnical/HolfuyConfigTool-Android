package com.holfuy.configtool.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.holfuy.configtool.device.HolfuyDevice

class MainViewModelFactory(
    private val device: HolfuyDevice
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(device) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}