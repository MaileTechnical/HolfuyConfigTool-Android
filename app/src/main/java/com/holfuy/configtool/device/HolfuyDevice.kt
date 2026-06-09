package com.holfuy.configtool.device

interface HolfuyDevice
{
    suspend fun connect(): Boolean

    suspend fun getFirmwareVersion(): String
}