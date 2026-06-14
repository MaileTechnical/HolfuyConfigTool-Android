package com.holfuy.configtool.device

import android.hardware.usb.UsbManager
import android.util.Log
import com.holfuy.configtool.protocol.ISPManager
import com.holfuy.configtool.usb.UsbDeviceProvider
import com.holfuy.configtool.protocol.ISPCommandTool
import com.holfuy.configtool.protocol.ISPCommands


class RealHolfuyDevice(
    private val usbManager: UsbManager,
    private val usbDeviceProvider: UsbDeviceProvider
) : HolfuyDevice
{
    override suspend fun connect(): Boolean
    {
        val usbDevice =
            usbDeviceProvider.findDevice()
                ?: return false

        Log.d(
            "HolfuyUSB",
            "Using USB device: ${usbDevice.deviceName}"
        )

        if (
            !ISPManager.openUsbSession(
                usbManager,
                usbDevice
            )
        ) {
            Log.e(
                "HolfuyUSB",
                "openUsbSession failed"
            )
            return false
        }

        val connectResult =
            ISPManager.suspendCMD_CONNECT()

        if (connectResult.isTimeout) {
            Log.e(
                "HolfuyUSB",
                "CMD_CONNECT timeout"
            )
            return false
        }

        if (!connectResult.isChecksum) {
            Log.e(
                "HolfuyUSB",
                "CMD_CONNECT checksum failure"
            )
            return false
        }

        val syncResult =
            ISPManager.suspendCMD_SYNC_PACKNO()

        if (!syncResult.isChecksum) {
            Log.e(
                "HolfuyUSB",
                "CMD_SYNC_PACKNO checksum failure"
            )
            return false
        }

        return true
    }

    override suspend fun getFirmwareVersion(): String
    {
        val result =
            ISPManager.suspendCMD_GET_FWVER()
    
        if (!result.isChecksum) {
            return "Checksum Error"
        }
    
        val buffer =
            result.buffer ?: return "No Response"
            
        readConfig()
    
        return ISPCommandTool.toFirmwareVersion(
            buffer
        )
    }
    
    private suspend fun readConfig()
    {
        val buffer =
            ISPManager.suspendCMD_READ_CONFIG()
    
        if (buffer == null) {
    
            Log.e(
                "HolfuyUSB",
                "READ_CONFIG returned null"
            )
    
            return
        }
    
        Log.i(
            "HolfuyUSB",
            "READ_CONFIG length=${buffer.size}"
        )
    
        Log.i(
            "HolfuyUSB",
            "READ_CONFIG raw=${
                buffer.joinToString(" ") {
                    "%02X".format(it)
                }
            }"
        )
    }
    
    override suspend fun updateFirmware(
        firmwareBytes: ByteArray,
        onProgress: (Int) -> Unit
    ): Boolean    
    {
        Log.i(
            "HolfuyUSB",
            "Starting firmware update (${firmwareBytes.size} bytes)"
        )
    
        var success = true
    
        ISPManager.sendCMD_UPDATE_BIN(
            ISPCommands.CMD_UPDATE_APROM,
            firmwareBytes,
            0u
        ) { _, progress ->
        
            Log.i(
                "HolfuyUSB",
                "UPDATE_BIN progress=$progress"
            )
        
            onProgress(progress)
        
            if (progress < 0) {
                success = false
            }
        }
    
        Log.i(
            "HolfuyUSB",
            "Firmware update finished success=$success"
        )
    
        return success
    }
}