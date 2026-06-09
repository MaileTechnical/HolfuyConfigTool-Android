package com.holfuy.configtool.ui.state

data class MainUiState(
    val connected: Boolean = false,
    val connecting: Boolean = false,
    val firmwareVersion: String = "--",
    val firmwareFile: String = "No file selected",
    val errorMessage: String? = null
)