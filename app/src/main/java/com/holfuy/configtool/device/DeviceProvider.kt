package com.holfuy.configtool.device

object DeviceProvider
{
    fun createDevice(): HolfuyDevice
    {
        return FakeHolfuyDevice()
    }
}