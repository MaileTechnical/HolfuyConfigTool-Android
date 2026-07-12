## Requirements

To use this tool, you will need:

* An Android phone or tablet with USB OTG (On-The-Go) capability.  OTG enables a phone or tablet to act as a host computer when connected to a weather station.
* A [USB OTG adapter](https://www.amazon.com/dp/B09SZ5NHF4?ref_=ppx_hzsearch_conn_dt_b_fed_asin_title_1&th=1) if your device does not have native USB host capability available through its connector.
* A USB cable with a Micro-B connector for the Holfuy station and a connector compatible with your Android device (USB-A or USB-C).
* A firmware file stored on your Android device in a convenient location, such as the Downloads folder.  It is important to know where this file is located because you will have only ~30 seconds to locate and select it from within the app.

## Firmware Update Procedure

1. Turn off the Holfuy station.
2. Disconnect the solar panel from the station main board.
3. Connect the station to the Android device using the USB cable and OTG adapter if required.
4. Turn on the station, and within 45 seconds, execute each of the next steps.  
5. Tap **Connect**.
6. When Android requests permission to access the USB device, grant permission.
7. Tap **Select Firmware** and browse to the firmware file you want to install.
8. Tap **Update Firmware**.
9. Wait for the update to complete. Do not disconnect the Android device from the station during the update process.
10. Turn off the station.
11. Disconnect the USB cable.
12. Reconnect the solar panel to the station.
13. Turn on the station.

The station should now be running the new firmware.

## Troubleshooting

First, ensure the station is connected through a USB OTG-capable connection.
Some Android devices with native OTG capability require a change in the settings to enable OTG.

This app communicates with the bootloader.
After roughly one minute of inactivity from the app, the bootloader disconnects from the Android
device and boots the existing application firmware.
If the LEDs on the main board are flashing, the bootloader is no longer running.
This can occur at any step in the process when the app is waiting for a response from the user.
If the **Connect**, **Select Firmware**, or **Update Firmware** buttons are not enabled when you
expect them to be enabled, the most likely cause is that the bootloader timer expired, and the
simplest thing to try is:

1. Turn off the station.
2. Wait two seconds.
3. Turn on the station.  
4. Continue with the process described above, starting with tapping **Connect**.

### Firmware update fails

Verify that:

* The selected firmware file is intended for your station.
* The USB cable is securely connected.
* The station remains connected to the Android device throughout the update process.