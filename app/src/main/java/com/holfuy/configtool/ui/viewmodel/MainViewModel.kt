package com.holfuy.configtool.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holfuy.configtool.device.HolfuyDevice
import com.holfuy.configtool.ui.state.MainUiState
import com.holfuy.configtool.usb.UsbDeviceProvider
import kotlinx.coroutines.launch

class MainViewModel(
    private val device: HolfuyDevice,
    private val usbDeviceProvider: UsbDeviceProvider
) : ViewModel()
{
    var uiState by mutableStateOf(MainUiState())
        private set
        
    fun connect()
    {

        viewModelScope.launch {
            val usbDevice = usbDeviceProvider.findDevice()
            
            Log.d(
                "HolfuyUSB",
                if (usbDevice != null)
                    "Found USB device: ${usbDevice.deviceName}"
                else
                    "No USB device found"
            )
    
            uiState = uiState.copy(
                connecting = true,
                errorMessage = null
            )
    
            try {
    
                val connected = device.connect()
    
                if (connected) {
    
                    val version = device.getFirmwareVersion()
    
                    uiState = uiState.copy(
                        connected = true,
                        connecting = false,
                        firmwareVersion = version
                    )
                }
                else {
    
                    uiState = uiState.copy(
                        connecting = false,
                        errorMessage = "Connection failed"
                    )
                }
            }
            catch (e: Exception) {
    
                uiState = uiState.copy(
                    connecting = false,
                    errorMessage = e.message
                )
            }
        }
    }
}