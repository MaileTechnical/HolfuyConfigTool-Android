package com.holfuy.configtool

import android.net.Uri
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.content.res.Configuration
import android.content.Intent
import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.holfuy.configtool.device.DeviceRepository
import com.holfuy.configtool.device.RealHolfuyDevice
import com.holfuy.configtool.ui.screens.MainScreen
import com.holfuy.configtool.ui.theme.HolfuyConfigToolTheme
import com.holfuy.configtool.ui.viewmodel.MainViewModel
import com.holfuy.configtool.ui.viewmodel.MainViewModelFactory
import com.holfuy.configtool.usb.AndroidUsbDeviceProvider
import com.holfuy.configtool.usb.HolfuyUsb

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
    
                    val usbDevice =
                        intent.getParcelableExtra(
                            UsbManager.EXTRA_DEVICE,
                            UsbDevice::class.java
                        ) ?: return
    
                    if (!HolfuyUsb.isSupported(usbDevice)) {
    
                        Log.i(
                            "HolfuyUSB",
                            "Ignoring permission response for unsupported device productId=0x${usbDevice.productId.toString(16)}"
                        )
    
                        return
                    }
    
                    val granted =
                        intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED,
                            false
                        )
    
                    Log.i(
                        "HolfuyUSB",
                        "USB permission response received granted=$granted"
                    )
    
                    DeviceRepository.setPermissionGranted(
                        granted
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
    
                    val usbDevice =
                        intent.getParcelableExtra(
                            UsbManager.EXTRA_DEVICE,
                            UsbDevice::class.java
                        ) ?: return
    
                    if (!HolfuyUsb.isSupported(usbDevice)) {
    
                        Log.i(
                            "HolfuyUSB",
                            "Ignoring unsupported USB device productId=0x${usbDevice.productId.toString(16)}"
                        )
    
                        return
                    }
    
                    Log.i(
                        "HolfuyUSB",
                        "Supported USB device attached"
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
    
                    if (!usbManager.hasPermission(usbDevice)) {
    
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
    
                    val usbDevice =
                        intent.getParcelableExtra(
                            UsbManager.EXTRA_DEVICE,
                            UsbDevice::class.java
                        ) ?: return
    
                    if (!HolfuyUsb.isSupported(usbDevice)) {
    
                        Log.i(
                            "HolfuyUSB",
                            "Ignoring detach of unsupported USB device productId=0x${usbDevice.productId.toString(16)}"
                        )
    
                        return
                    }
    
                    Log.i(
                        "HolfuyUSB",
                        "Supported USB device detached"
                    )
    
                    DeviceRepository.clearConnectionState()
    
                    Log.i(
                        "HolfuyUSB",
                        "DeviceRepository state=${DeviceRepository.state}"
                    )
                }
            }
        }
    
    override fun onCreate(savedInstanceState: Bundle?)
    {
    
        Log.i(
            "HolfuyUSB",
            "MainActivity onCreate savedInstanceState=${savedInstanceState != null}"
        )
        
        val config = resources.configuration
        
        Log.i(
            "HolfuyUSB",
            "Config keyboard=${config.keyboard} " +
            "hardKeyboardHidden=${config.hardKeyboardHidden} " +
            "navigation=${config.navigation}"
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
                    
                val deviceState by viewModel.deviceStateFlow.collectAsState()                               
                
                MainScreen(
                    uiState = viewModel.uiState,
                    deviceState = deviceState,
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
            "MainActivity onDestroy changingConfigurations=$isChangingConfigurations"
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
    
    override fun onConfigurationChanged(
        newConfig: Configuration
    )
    {
        super.onConfigurationChanged(newConfig)
    
        Log.i(
            "HolfuyUSB",
            "onConfigurationChanged keyboard=${newConfig.keyboard} " +
            "hardKeyboardHidden=${newConfig.hardKeyboardHidden} " +
            "navigation=${newConfig.navigation}"
        )
    }
}