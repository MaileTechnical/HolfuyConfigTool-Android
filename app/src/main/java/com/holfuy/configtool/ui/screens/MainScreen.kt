package com.holfuy.configtool.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.holfuy.configtool.ui.state.MainUiState

@Composable
fun MainScreen(
    uiState: MainUiState,
    onConnectClick: () -> Unit,
    onSelectFirmwareClick: () -> Unit,
    onUpdateFirmwareClick: () -> Unit
)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    )
    {
        Text(
            text = "Holfuy Config Tool",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            enabled = 
                uiState.canConnect && 
                !uiState.connected &&
                !uiState.updateInProgress,
            onClick = onConnectClick
        ) {
            Text(
                if (uiState.connecting)
                    "Connecting..."
                else
                    "Connect"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Connection Status")
                Text(
                    if (uiState.connected)
                        "Connected"
                    else
                        "Disconnected"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                  Text("Firmware File")
                  
                  Text(uiState.firmwareFile)
                  
                  uiState.firmwareSize?.let {
                      Text("Size: $it bytes")
                  }
              }
          
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = 
                uiState.canSelectFirmware &&
                !uiState.updateInProgress,
            onClick = onSelectFirmwareClick
        ) {
            Text("Select Firmware")
        }

        Spacer(modifier = Modifier.height(8.dp))
        
        if (uiState.updateInProgress) {
        
            Spacer(modifier = Modifier.height(16.dp))
        
            LinearProgressIndicator(
                progress = {
                    uiState.updateProgress / 100f
                },
                modifier = Modifier.fillMaxWidth()
            )
        
            Text(
                "${uiState.updateProgress}%"
            )
        }
        
        if (uiState.updateCompleted) {
        
            Text(
                "Firmware update complete"
            )
        }

        Button(
            enabled = 
                uiState.canUpdateFirmware &&
                !uiState.updateInProgress,
            onClick = onUpdateFirmwareClick
        ) {
            Text("Update Firmware")
        }
    }
}