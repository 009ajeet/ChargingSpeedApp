// filepath: c:\Users\91807\Desktop\python script\ChargingSpeedApp\app\src\main\java\com\example\chargingspeedapp\ChargingSpeedManager.kt
package com.example.chargingspeedapp

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log

class ChargingSpeedManager(private val context: Context) {

    // Getting actual charging current in mAh is complex and not reliably available
    // through standard Android APIs for all devices.
    // This function provides a placeholder or uses available properties if possible.
    fun getChargingSpeed(): Long {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

        // Try getting average current first (newer APIs, might not be supported)
        var currentAvg = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE)
        if (currentAvg != Long.MIN_VALUE && currentAvg != 0L) {
             Log.d("ChargingSpeedManager", "Using Average Current: $currentAvg microamperes")
             return currentAvg / 1000 // Convert microamperes to milliamperes
        }

        // Fallback to current_now (might be instantaneous, less stable)
        var currentNow = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
        if (currentNow != Long.MIN_VALUE && currentNow != 0L) {
             Log.d("ChargingSpeedManager", "Using Current Now: $currentNow microamperes")
             return currentNow / 1000 // Convert microamperes to milliamperes
        }

        // If properties are not available or return 0/MIN_VALUE, return a placeholder
        Log.w("ChargingSpeedManager", "Could not retrieve charging current via BatteryManager properties.")
        return getChargingStatusFromIntent() // Try intent as a last resort (less reliable for current)

    }

    // Less reliable method using Intent sticky broadcast (might not give current)
    private fun getChargingStatusFromIntent(): Long {
         val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }
        // Check if charging
         val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
         val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                                  status == BatteryManager.BATTERY_STATUS_FULL

        if (isCharging) {
             // Intent doesn't reliably provide charging current in mAh.
             // Returning a placeholder value when charging is detected.
             Log.w("ChargingSpeedManager", "Using Intent status: Charging detected, but current unavailable. Returning placeholder.")
             return -1 // Indicate charging, but unknown speed
        } else {
             Log.d("ChargingSpeedManager", "Using Intent status: Not charging.")
             return 0 // Not charging
        }
    }
}