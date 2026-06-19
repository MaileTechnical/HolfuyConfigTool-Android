package com.holfuy.configtool.device

object DeviceRepository
{
    var state = DeviceState()

    fun setPermissionGranted(
        granted: Boolean
    )
    {
        state = state.copy(
            permissionGranted = granted
        )
    }
}