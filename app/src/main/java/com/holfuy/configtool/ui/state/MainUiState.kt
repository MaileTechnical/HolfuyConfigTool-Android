package com.holfuy.configtool.ui.state

data class MainUiState(
    val connected: Boolean = false,
    val connecting: Boolean = false,
    val firmwareVersion: String = "--",
    val firmwareFile: String = "No file selected",
    val firmwareFileName: String? = null,
    val firmwareSize: Int? = null,
    val errorMessage: String? = null,
    val canConnect: Boolean = true,
    val canSelectFirmware: Boolean = false,
    val canUpdateFirmware: Boolean = false,
    val updateInProgress: Boolean = false,
    val updateProgress: Int = 0,
    val updateCompleted: Boolean = false
)