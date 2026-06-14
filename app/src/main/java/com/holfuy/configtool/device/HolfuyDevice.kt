package com.holfuy.configtool.device

interface HolfuyDevice
{
    suspend fun connect(): Boolean

    suspend fun updateFirmware(
        firmwareBytes: ByteArray,
        onProgress: (Int) -> Unit
    ): Boolean
}