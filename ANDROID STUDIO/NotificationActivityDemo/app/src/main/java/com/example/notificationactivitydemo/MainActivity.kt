package com.example.notificationactivitydemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Build.*
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val channelID = "com.example.android.notifydemo.channel1"
    private var notificationManager: NotificationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        createNotificationChannel(channelID, "DemoChannel", "This is a demo")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelID, "DemoChannel", "This is a demo")
        findViewById<Button>(R.id.button).setOnClickListener {
            displayNotification()
        }

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            displayNotification()
        }

    }

    private fun createNotificationChannel(id: String, name: String, channelDescription: String) {
        if(VERSION.SDK_INT >= VERSION_CODES.O) {
            val inportance: Int = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, inportance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)
        }

    }

    private fun displayNotification() {
        val notificationID = 45
        val notification = NotificationCompat.Builder(this@MainActivity, channelID)
            .setContentTitle("Demo Title")
            .setContentText("This is a demo notification")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

    }
}