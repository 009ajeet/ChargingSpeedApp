package com.example.chargingspeedapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.SeekBar
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var chargingSpeedManager: ChargingSpeedManager
    private lateinit var volumeController: VolumeController
    private lateinit var chargingSpeedTextView: TextView
    private lateinit var volumeSeekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chargingSpeedManager = ChargingSpeedManager(this)
        volumeController = VolumeController(this)

        chargingSpeedTextView = findViewById(R.id.chargingSpeedTextView)
        volumeSeekBar = findViewById(R.id.volumeSeekBar)

        updateChargingSpeed()
        setupVolumeControl()
    }

    private fun updateChargingSpeed() {
        val chargingSpeed = chargingSpeedManager.getChargingSpeed()
        chargingSpeedTextView.text = getString(R.string.charging_speed, chargingSpeed)
    }

    private fun setupVolumeControl() {
        volumeSeekBar.max = 100 // Assuming volume is from 0 to 100
        volumeSeekBar.progress = volumeController.getCurrentVolume()

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                volumeController.setVolume(progress)
                Toast.makeText(this@MainActivity, "Volume set to $progress", Toast.LENGTH_SHORT).show()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}