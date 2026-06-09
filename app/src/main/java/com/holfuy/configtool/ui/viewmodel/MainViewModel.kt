package com.holfuy.configtool.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.holfuy.configtool.ui.state.MainUiState

class MainViewModel : ViewModel()
{
    var uiState by mutableStateOf(MainUiState())
        private set
        
    fun simulateConnection()
    {
        uiState = uiState.copy(
            connected = true,
            firmwareVersion = "2.7.3"
        )
    }
}