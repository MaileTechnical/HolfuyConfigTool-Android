package com.holfuy.configtool.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.holfuy.configtool.device.DeviceRepository
import com.holfuy.configtool.device.HolfuyDevice
import com.holfuy.configtool.ui.state.MainUiState
import com.holfuy.configtool.usb.UsbDeviceProvider

class MainViewModel(
    private val device: HolfuyDevice,
    private val usbDeviceProvider: UsbDeviceProvider
) : ViewModel()
{
    var uiState by mutableStateOf(MainUiState())
        private set
        
    private var firmwareBytes: ByteArray? = null
    val deviceStateFlow = DeviceRepository.stateFlow
    
    fun setFirmware(
        fileName: String,
        bytes: ByteArray
    )
    {
        firmwareBytes = bytes
    
        uiState = uiState.copy(
            firmwareFile = fileName,
            firmwareFileName = fileName,
            firmwareSize = bytes.size,
        )
    }
        
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
                
                    DeviceRepository.setConnected(
                        true
                    )
                
                    Log.i(
                        "HolfuyUSB",
                        "DeviceRepository state=${DeviceRepository.state}"
                    )
                
                    uiState = uiState.copy(
                        connecting = false
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
    
    fun updateFirmware()
    {
        val bytes = firmwareBytes ?: return
    
        viewModelScope.launch(Dispatchers.IO) {
        
            DeviceRepository.setUpdateInProgress(
                true
            )
            
            DeviceRepository.setUpdateProgress(
                0
            )
            
            Log.i(
                "HolfuyUSB",
                "DeviceRepository state=${DeviceRepository.state}"
            )
        
            uiState = uiState.copy(
                updateCompleted = false
            )
            
            val success =
                device.updateFirmware(
                    bytes
                ) { progress ->
                
                    DeviceRepository.setUpdateProgress(
                        progress
                    )
                
                    Log.i(
                        "HolfuyUSB",
                        "Update progress=$progress repository=${DeviceRepository.state}"
                    )
                } 
                
            DeviceRepository.setUpdateInProgress(
                false
            )
            
            Log.i(
                "HolfuyUSB",
                "DeviceRepository state=${DeviceRepository.state}"
            )     
                 
            uiState = uiState.copy(
                updateCompleted = success
            )  
            
            Log.i("HolfuyUSB", "updateFirmware success=$success")
        }
    }
}