package com.holfuy.configtool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.holfuy.configtool.device.FakeHolfuyDevice
import com.holfuy.configtool.ui.screens.MainScreen
import com.holfuy.configtool.ui.theme.HolfuyConfigToolTheme
import com.holfuy.configtool.ui.viewmodel.MainViewModel
import com.holfuy.configtool.ui.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HolfuyConfigToolTheme {
        
                val factory = remember {
                    MainViewModelFactory(
                        FakeHolfuyDevice()
                    )
                }
        
                val viewModel: MainViewModel = viewModel(
                    factory = factory
                )
        
                MainScreen(
                    uiState = viewModel.uiState,
                    onConnectClick = {
                        viewModel.connect()
                    }
                )
            }
        }
    }
}