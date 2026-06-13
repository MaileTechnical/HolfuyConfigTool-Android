package com.holfuy.configtool

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.util.Log
import android.content.Intent
import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.holfuy.configtool.device.RealHolfuyDevice
import com.holfuy.configtool.ui.screens.MainScreen
import com.holfuy.configtool.ui.theme.HolfuyConfigToolTheme
import com.holfuy.configtool.ui.viewmodel.MainViewModel
import com.holfuy.configtool.ui.viewmodel.MainViewModelFactory
import com.holfuy.configtool.usb.AndroidUsbDeviceProvider

class MainActivity : ComponentActivity()
{
    companion object
    {
        const val ACTION_USB_PERMISSION =
            "com.holfuy.configtool.USB_PERMISSION"
    }
    
    private lateinit var permissionIntent: PendingIntent 
    
    private val usbPermissionReceiver =
        object : BroadcastReceiver()
        {
            override fun onReceive(
                context: Context,
                intent: Intent
            )
            {
                if (intent.action == ACTION_USB_PERMISSION) {
    
                    Log.i(
                        "HolfuyUSB",
                        "USB permission response received"
                    )
                }
            }
        }
    
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        
        registerReceiver(
            usbPermissionReceiver,
            IntentFilter(ACTION_USB_PERMISSION),
            RECEIVER_NOT_EXPORTED
        )        
   
        permissionIntent = PendingIntent.getBroadcast(
                this,
                0,
                Intent(ACTION_USB_PERMISSION),
                PendingIntent.FLAG_IMMUTABLE
        )

        setContent {
            HolfuyConfigToolTheme {

                val usbManager =
                    applicationContext.getSystemService(
                        Context.USB_SERVICE
                    ) as UsbManager
                
                val usbDeviceProvider = remember {
                    AndroidUsbDeviceProvider(usbManager)
                }

                val device = remember {
                    RealHolfuyDevice(
                        usbManager,
                        usbDeviceProvider
                    )
                }

                val factory = remember {
                    MainViewModelFactory(
                        device,
                        usbDeviceProvider
                    )
                }

                val viewModel: MainViewModel = viewModel(
                    factory = factory
                )
                
                MainScreen(
                    uiState = viewModel.uiState,
                    onConnectClick = {
                
                        val usbDevice =
                            usbDeviceProvider.findDevice()
                
                        if (usbDevice == null) {
                
                            Log.i(
                                "HolfuyUSB",
                                "No USB device found"
                            )
                
                            viewModel.connect()
                        }
                        else if (!usbManager.hasPermission(usbDevice)) {
                
                            Log.i(
                                "HolfuyUSB",
                                "Requesting USB permission"
                            )
                
                            usbManager.requestPermission(
                                usbDevice,
                                permissionIntent
                            )
                        }
                        else {
                
                            Log.i(
                                "HolfuyUSB",
                                "USB permission already granted"
                            )
                
                            viewModel.connect()
                        }
                    }
                )
            }
        }
    }
    
    override fun onDestroy()
    {
        unregisterReceiver(
            usbPermissionReceiver
        )
    
        super.onDestroy()
    }
}