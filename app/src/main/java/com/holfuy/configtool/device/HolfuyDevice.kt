package com.holfuy.configtool.device

interface HolfuyDevice
{
    suspend fun connect(): Boolean

    suspend fun getFirmwareVersion(): String

    suspend fun updateFirmware(
        firmwareBytes: ByteArray,
        onProgress: (Int) -> Unit
    ): Boolean
}