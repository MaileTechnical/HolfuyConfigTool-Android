package com.holfuy.configtool.device

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
}