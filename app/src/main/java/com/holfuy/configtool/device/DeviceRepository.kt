package com.holfuy.configtool.device

object DeviceRepository
{
    var state = DeviceState()

    fun setAttached(
        attached: Boolean
    )
    {
        state = state.copy(
            attached = attached
        )
    }
    
    fun setPermissionGranted(
        granted: Boolean
    )
    {
        state = state.copy(
            permissionGranted = granted
        )
    }
    
    fun setConnected(
        connected: Boolean
    )
    {
        state = state.copy(
            connected = connected
        )
    }
    
    fun clearConnectionState()
    {
        state = state.copy(
            attached = false,
            permissionGranted = false,
            connected = false
        )
    }
}