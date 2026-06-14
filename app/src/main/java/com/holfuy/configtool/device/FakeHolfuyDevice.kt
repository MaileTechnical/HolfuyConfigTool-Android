package com.holfuy.configtool.device

import android.util.Log
import kotlinx.coroutines.delay

class FakeHolfuyDevice : HolfuyDevice
{
    override suspend fun connect(): Boolean
    {
        delay(5000)
        return true
    }

    override suspend fun getFirmwareVersion(): String
    {
        delay(250)
        return "2.7.3"
    }
    
    override suspend fun updateFirmware(
        firmwareBytes: ByteArray
    ): Boolean
    {
        Log.i(
            "HolfuyUSB",
            "Starting firmware update (${firmwareBytes.size} bytes)"
        )
    
        var success = true
    
        Log.i(
            "HolfuyUSB",
            "Firmware update finished success=$success"
        )
    
        return success
    }
}