package com.holfuy.configtool.device

class RealHolfuyDevice : HolfuyDevice
{
    override suspend fun connect(): Boolean
    {
        return true
    }

    override suspend fun getFirmwareVersion(): String
    {
        return "real-device-not-implemented"
    }
}