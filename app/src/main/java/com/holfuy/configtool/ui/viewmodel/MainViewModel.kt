package com.holfuy.configtool.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holfuy.configtool.device.HolfuyDevice
import com.holfuy.configtool.ui.state.MainUiState
import kotlinx.coroutines.launch

class MainViewModel(
    private val device: HolfuyDevice
) : ViewModel()
{
    var uiState by mutableStateOf(MainUiState())
        private set
        
    fun connect()
    {
        viewModelScope.launch {
    
            uiState = uiState.copy(
                connecting = true,
                errorMessage = null
            )
    
            try {
    
                val connected = device.connect()
    
                if (connected) {
    
                    val version = device.getFirmwareVersion()
    
                    uiState = uiState.copy(
                        connected = true,
                        connecting = false,
                        firmwareVersion = version
                    )
                }
                else {
    
                    uiState = uiState.copy(
                        connecting = false,
                        errorMessage = "Connection failed"
                    )
                }
            }
            catch (e: Exception) {
    
                uiState = uiState.copy(
                    connecting = false,
                    errorMessage = e.message
                )
            }
        }
    }
}