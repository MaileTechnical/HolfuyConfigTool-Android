package com.holfuy.configtool.device

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object DeviceRepository
{
    private val _state =
        MutableStateFlow(
            DeviceState()
        )

    val stateFlow: StateFlow<DeviceState> =
        _state.asStateFlow()

    var state: DeviceState
        get() = _state.value
        set(value)
        {
            _state.value = value
        }

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

    fun setUpdateInProgress(
        inProgress: Boolean
    )
    {
        state = state.copy(
            updateInProgress = inProgress
        )
    }

    fun setUpdateProgress(
        progress: Int
    )
    {
        state = state.copy(
            updateProgress = progress
        )
    }

    fun clearConnectionState()
    {
        state = state.copy(
            attached = false,
            permissionGranted = false,
            connected = false,
            updateInProgress = false,
            updateProgress = 0
        )
    }
}