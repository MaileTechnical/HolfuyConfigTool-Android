package com.holfuy.configtool.device

import android.hardware.usb.UsbManager
import android.util.Log
import com.holfuy.configtool.protocol.ISPCommands
import com.holfuy.configtool.protocol.ISPManager
import com.holfuy.configtool.usb.UsbDeviceProvider


class RealHolfuyDevice(
    private val usbManager: UsbManager,
    private val usbDeviceProvider: UsbDeviceProvider
) : HolfuyDevice
{

    companion object {
        private const val TAG = "HolfuyUSB-RHD"
    }
    
    override suspend fun connect(): Boolean
    {
        var connectionSucceeded = false
    
        try {
    
            val usbDevice =
                usbDeviceProvider.findDevice()
                    ?: return false
    
            Log.d(
                TAG,
                "Using USB device: ${usbDevice.deviceName}"
            )
    
            if (
                !ISPManager.openUsbSession(
                    usbManager,
                    usbDevice
                )
            ) {
    
                Log.e(
                    TAG,
                    "openUsbSession failed"
                )
    
                return false
            }
    
            val connectResult =
                ISPManager.suspendCMD_CONNECT()
    
            if (connectResult.isTimeout) {
    
                Log.e(
                    TAG,
                    "CMD_CONNECT timeout"
                )
    
                return false
            }
    
            if (!connectResult.isChecksum) {
    
                Log.e(
                    TAG,
                    "CMD_CONNECT checksum failure"
                )
    
                return false
            }
    
            val syncResult =
                ISPManager.suspendCMD_SYNC_PACKNO()
    
            if (!syncResult.isChecksum) {
    
                Log.e(
                    TAG,
                    "CMD_SYNC_PACKNO checksum failure"
                )
    
                return false
            }
    
            connectionSucceeded = true
            return true
        }
        finally {
    
            DeviceRepository.setConnected(connectionSucceeded)
        }
    }
   
    override suspend fun updateFirmware(
        firmwareBytes: ByteArray,
        onProgress: (Int) -> Unit
    ): Boolean    
    {
        Log.i(
            TAG,
            "Starting firmware update (${firmwareBytes.size} bytes)"
        )
    
        var success = true
    
        ISPManager.sendCMD_UPDATE_BIN(
            ISPCommands.CMD_UPDATE_APROM,
            firmwareBytes,
            0u
        ) { _, progress ->
        
            onProgress(progress)
        
            if (progress < 0) {
                success = false
            }
        }
    
        Log.i(
            TAG,
            "Firmware update finished success=$success"
        )
    
        return success
    }
    
    override fun onUsbDetached()
    {
        Log.i(
            TAG,
            "onUsbDetached()"
        )
    
        ISPManager.closeUsbSession()
    }
}