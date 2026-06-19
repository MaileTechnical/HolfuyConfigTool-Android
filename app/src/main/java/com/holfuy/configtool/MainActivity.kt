package com.holfuy.configtool

import android.net.Uri
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.util.Log
import android.content.Intent
import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.holfuy.configtool.device.DeviceRepository
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
    private lateinit var mainViewModel: MainViewModel
    
    private val usbPermissionReceiver =
        object : BroadcastReceiver()
        {
            override fun onReceive(
                context: Context,
                intent: Intent
            )
            {
                Log.i(
                    "HolfuyUSB",
                    "usbPermissionReceiver action=${intent.action}"
                )
    
                if (intent.action == ACTION_USB_PERMISSION) {
    
                    val granted =
                        intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED,
                            false
                        )
    
                    Log.i(
                        "HolfuyUSB",
                        "USB permission response received granted=$granted"
                    )
                }
            }
        }

    private val usbAttachReceiver =
        object : BroadcastReceiver()
        {
            override fun onReceive(
                context: Context,
                intent: Intent
            )
            {
                if (
                    intent.action ==
                    UsbManager.ACTION_USB_DEVICE_ATTACHED
                ) {
    
                    Log.i(
                        "HolfuyUSB",
                        "USB device attached"
                    )
                    
                    DeviceRepository.setAttached( 
                        true 
                    )                      
                    
                    Log.i(
                        "HolfuyUSB",
                        "DeviceRepository state=${DeviceRepository.state}"
                    )
    
                    val usbManager =
                        context.getSystemService(
                            Context.USB_SERVICE
                        ) as UsbManager
    
                    val usbDevice =
                        intent.getParcelableExtra(
                            UsbManager.EXTRA_DEVICE,
                            UsbDevice::class.java
                        )
    
                    if (
                        usbDevice != null &&
                        !usbManager.hasPermission(usbDevice)
                    ) {
    
                        Log.i(
                            "HolfuyUSB",
                            "Requesting USB permission from attach event"
                        )
    
                        usbManager.requestPermission(
                            usbDevice,
                            permissionIntent
                        )
                    }
                }
            }
        }

        private val usbDetachReceiver =
            object : BroadcastReceiver()
            {
                override fun onReceive(
                    context: Context,
                    intent: Intent
                )
                {
                    if (
                        intent.action ==
                        UsbManager.ACTION_USB_DEVICE_DETACHED
                    ) {
        
                        Log.i(
                            "HolfuyUSB",
                            "USB device detached"
                        )
                        DeviceRepository.clearConnectionState()
                        
                        Log.i(
                            "HolfuyUSB",
                            "DeviceRepository state=${DeviceRepository.state}"
                        )
        
                        mainViewModel.onUsbDetached()
                    }
                }
            }
    
    override fun onCreate(savedInstanceState: Bundle?)
    {
    
        Log.i(
            "HolfuyUSB",
            "MainActivity onCreate"
        )
        
        super.onCreate(savedInstanceState)
        
        Log.i(
            "HolfuyUSB",
            "DeviceRepository state=${DeviceRepository.state}"
        )
        
        registerReceiver(
            usbPermissionReceiver,
            IntentFilter(ACTION_USB_PERMISSION),
            RECEIVER_NOT_EXPORTED
        )
        
        registerReceiver(
            usbAttachReceiver,
            IntentFilter(
                UsbManager.ACTION_USB_DEVICE_ATTACHED
            ),
            RECEIVER_NOT_EXPORTED
        )
        
        registerReceiver(
            usbDetachReceiver,
            IntentFilter(
                UsbManager.ACTION_USB_DEVICE_DETACHED
            ),
            RECEIVER_NOT_EXPORTED
        )
        
        Log.i(
            "HolfuyUSB",
            "usbPermissionReceiver registered"
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
                mainViewModel = viewModel
                
                val firmwarePicker =
                    rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()
                    ) { uri: Uri? ->
                
                        if (uri == null) {
                            return@rememberLauncherForActivityResult
                        }
                
                        val bytes =
                            contentResolver
                                .openInputStream(uri)
                                ?.readBytes()
                                ?: return@rememberLauncherForActivityResult
                
                        val fileName =
                            uri.lastPathSegment ?: "firmware.bin"
                
                        Log.i(
                            "HolfuyUSB",
                            "Loaded firmware: $fileName (${bytes.size} bytes)"
                        )
                
                        viewModel.setFirmware(
                            fileName,
                            bytes
                        )
                    }                
                
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
                    },
                        
                    onSelectFirmwareClick = {
                    
                        firmwarePicker.launch("*/*")
                    
                    },
                    
                    onUpdateFirmwareClick = viewModel::updateFirmware         
                )
            }
        }
    }
    
    override fun onDestroy()
    {
    
        Log.i(
            "HolfuyUSB",
            "MainActivity onDestroy"
        )
        
        unregisterReceiver(
            usbPermissionReceiver
        )
        
        unregisterReceiver(
            usbAttachReceiver
        )
        
        unregisterReceiver(
            usbDetachReceiver
        )
    
        super.onDestroy()
    }
    
    override fun onResume()
    {
        super.onResume()
    
        Log.i(
            "HolfuyUSB",
            "MainActivity onResume"
        )
    
        val usbManager =
            getSystemService(
                Context.USB_SERVICE
            ) as UsbManager
    
        val usbDevice =
            usbManager.deviceList
                .values
                .firstOrNull()
    
        val permissionGranted =
            usbDevice != null &&
            usbManager.hasPermission(
                usbDevice
            )
    
        DeviceRepository.setPermissionGranted(
            permissionGranted
        )
    
        Log.i(
            "HolfuyUSB",
            "DeviceRepository state=${DeviceRepository.state}"
        )
    }
}