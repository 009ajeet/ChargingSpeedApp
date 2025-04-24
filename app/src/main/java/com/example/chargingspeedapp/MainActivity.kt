// filepath: c:\Users\91807\Desktop\python script\ChargingSpeedApp\app\src\main\java\com\example\chargingspeedapp\MainActivity.kt
package com.example.chargingspeedapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.Toast
import com.example.chargingspeedapp.databinding.ActivityMainBinding // Import View Binding class
import android.media.AudioManager
import android.util.Log

class MainActivity : AppCompatActivity() {

    // Use View Binding
    private lateinit var binding: ActivityMainBinding

    private lateinit var chargingSpeedManager: ChargingSpeedManager
    private lateinit var volumeController: VolumeController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate layout using View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chargingSpeedManager = ChargingSpeedManager(this)
        volumeController = VolumeController(this)

        updateChargingSpeed()
        setupVolumeControl()
    }

    // Consider updating charging speed periodically or on specific events
    // (e.g., ACTION_POWER_CONNECTED / DISCONNECTED broadcasts)
    private fun updateChargingSpeed() {
        val chargingSpeed = chargingSpeedManager.getChargingSpeed()
        Log.d("MainActivity", "Retrieved charging speed: $chargingSpeed")
        val speedText = when {
             chargingSpeed > 0 -> "$chargingSpeed mAh"
             chargingSpeed == -1L -> "Charging (Speed N/A)" // Placeholder used
             else -> "Not Charging"
        }
         // Use binding to access views
        binding.chargingSpeedValue.text = speedText
    }

    private fun setupVolumeControl() {
        val maxVolumeSystem = volumeController.getMaxVolume()
        val currentVolumePercent = volumeController.getCurrentVolume() // Gets 0-100

        Log.d("MainActivity", "Max System Volume: $maxVolumeSystem, Current Volume %: $currentVolumePercent")

        // SeekBar uses 0-100 for progress
        binding.volumeControl.max = 100
        binding.volumeControl.progress = currentVolumePercent
        binding.volumeValue.text = getString(R.string.current_volume_label, currentVolumePercent) // Update label

        binding.volumeControl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    volumeController.setVolume(progress)
                    binding.volumeValue.text = getString(R.string.current_volume_label, progress) // Update label during change
                    Log.d("MainActivity", "Volume SeekBar changed to $progress")
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Optional: Show a toast when user finishes adjusting
                // Toast.makeText(this@MainActivity, "Call volume set to ${seekBar?.progress ?: 0}%", Toast.LENGTH_SHORT).show()
            }
        })
    }
}