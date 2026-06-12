package com.holfuy.configtool

import android.os.Bundle
import android.content.Context
import android.hardware.usb.UsbManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.holfuy.configtool.device.DeviceProvider
import com.holfuy.configtool.ui.screens.MainScreen
import com.holfuy.configtool.ui.theme.HolfuyConfigToolTheme
import com.holfuy.configtool.ui.viewmodel.MainViewModel
import com.holfuy.configtool.ui.viewmodel.MainViewModelFactory
import com.holfuy.configtool.usb.AndroidUsbDeviceProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HolfuyConfigToolTheme {
                val usbManager =
                    applicationContext.getSystemService(
                        Context.USB_SERVICE
                    ) as UsbManager
                
                val usbDeviceProvider = remember {
                    AndroidUsbDeviceProvider(usbManager)
                }
        
                val factory = remember {
                    MainViewModelFactory(
                        DeviceProvider.createDevice(),
                        usbDeviceProvider
                    )
                }
        
                val viewModel: MainViewModel = viewModel(
                    factory = factory
                )
        
                MainScreen(
                    uiState = viewModel.uiState,
                    onConnectClick = {
                        viewModel.connect()
                    }
                )
            }
        }
    }
}