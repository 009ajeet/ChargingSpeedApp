// filepath: c:\Users\91807\Desktop\python script\ChargingSpeedApp\app\src\main\java\com\example\chargingspeedapp\VolumeController.kt
package com.example.chargingspeedapp

import android.content.Context
import android.media.AudioManager
import android.util.Log

class VolumeController(context: Context) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val streamType = AudioManager.STREAM_VOICE_CALL // Control call volume

    fun setVolume(progress: Int) {
        val maxVolume = audioManager.getStreamMaxVolume(streamType)
        val targetVolume = (progress / 100.0 * maxVolume).toInt()
        try {
             audioManager.setStreamVolume(streamType, targetVolume, 0) // Flag 0 means no UI shown
             Log.d("VolumeController", "Setting call volume to $targetVolume (Progress: $progress)")
        } catch (e: SecurityException) {
             Log.e("VolumeController", "Permission denied to change audio settings.", e)
             // Handle lack of permission (e.g., show a message to the user)
        } catch (e: Exception) {
             Log.e("VolumeController", "Error setting volume", e)
        }
    }

    fun getCurrentVolume(): Int {
         try {
            val maxVolume = audioManager.getStreamMaxVolume(streamType)
            val currentVolume = audioManager.getStreamVolume(streamType)
            // Convert current volume to a percentage (0-100)
            return if (maxVolume > 0) (currentVolume * 100.0 / maxVolume).toInt() else 0
         } catch (e: Exception) {
             Log.e("VolumeController", "Error getting current volume", e)
             return 0 // Default to 0 on error
         }
    }

     fun getMaxVolume(): Int {
        return audioManager.getStreamMaxVolume(streamType)
    }
}