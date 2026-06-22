package com.holfuy.configtool.ui.state

import com.holfuy.configtool.device.DeviceState

data class MainUiState(
    val connecting: Boolean = false,
    val firmwareFile: String = "No file selected",
    val firmwareFileName: String? = null,
    val firmwareSize: Int? = null,
    val errorMessage: String? = null,
    val updateCompleted: Boolean = false
)