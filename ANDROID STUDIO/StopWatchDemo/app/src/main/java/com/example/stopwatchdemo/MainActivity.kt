package com.example.stopwatchdemo

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stopwatchdemo.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isStarted = false
    private var isPaused = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0
    private lateinit var logAdapter: LogAdapter

    private val notificationChannelId = "timer_notifications"
    private val notificationId = 1

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "TIMER"

        logAdapter = LogAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = logAdapter
        }

        binding.btnStart.setOnClickListener {
            binding.rvClear.visibility = View.VISIBLE
            startOrPause()
        }
        binding.btnReset.setOnClickListener {
            reset()
        }
        binding.rvClear.setOnClickListener {
            clearLogs()
        }

        serviceIntent = Intent(applicationContext, StopWatchService::class.java)
        Log.d("MainActivity", "Registering receiver")
        registerReceiver(updateTime, IntentFilter(StopWatchService.UPDATED_TIME), RECEIVER_EXPORTED)
        registerReceiver(notificationReceiver, IntentFilter(StopWatchService.NOTIFICATION_TIME), RECEIVER_EXPORTED)

        createNotificationChannel()

        if (savedInstanceState != null) {
            time = savedInstanceState.getDouble("time", 0.0)
            isStarted = savedInstanceState.getBoolean("isStarted", false)
            isPaused = savedInstanceState.getBoolean("isPaused", false)
            binding.tvTime.text = getFormattedTime(time)
            updateStartButtonText()
            Log.d("MainActivity", "Restored state: time=$time, isStarted=$isStarted, isPaused=$isPaused")
        }
    }

    private fun startOrPause() {
        if (isStarted) {
            if (isPaused) {
                start()
            } else {
                pause()
            }
        } else {
            start()
        }
    }

    private fun start() {
        Log.d("MainActivity", "Starting service with time: $time")
        serviceIntent.putExtra(StopWatchService.CURRENT_TIME, time)
        startService(serviceIntent)
        isStarted = true
        isPaused = false
        updateStartButtonText()
        logAction("Started")
        binding.btnReset.isEnabled = true // Enable the reset button
    }

    private fun pause() {
        Log.d("MainActivity", "Pausing service")
        stopService(serviceIntent)
        isPaused = true
        updateStartButtonText()
        logAction("Paused")
        binding.btnReset.isEnabled = true // Enable the reset button
    }

    private fun stop() {
        Log.d("MainActivity", "Stopping service")
        stopService(serviceIntent)
        isStarted = false
        isPaused = false
        updateStartButtonText()
    }

    private fun reset() {
        if (time > 0.0) {
            Log.d("MainActivity", "Resetting timer")
            logAction("Reset", true)
            stop()
            time = 0.0
            binding.tvTime.text = getFormattedTime(time)
            binding.btnReset.isEnabled = false // Disable the reset button
        }
    }

    private fun logAction(action: String, isReset: Boolean = false) {
        val timerDisplay = getFormattedTime(time)
        val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val logMessage = if (isReset) "$timerDisplay -> 00:00:00" else timerDisplay
        logAdapter.addLogEntry(LogEntry(action, logMessage, timestamp))
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(StopWatchService.CURRENT_TIME, 0.0)
            binding.tvTime.text = getFormattedTime(time)
            Log.d("MainActivity", "Time updated: $time")
        }
    }

    private val notificationReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            showNotification()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun getFormattedTime(time: Double): String {
        val timeInt = time.roundToInt()
        val hours = timeInt % 86400 / 3600
        val minutes = timeInt % 86400 % 3600 / 60
        val seconds = timeInt % 86400 % 3600 % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun updateStartButtonText() {
        binding.btnStart.text = when {
            isStarted && isPaused -> "Start"
            isStarted -> "Pause"
            else -> "Start"
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("time", time)
        outState.putBoolean("isStarted", isStarted)
        outState.putBoolean("isPaused", isPaused)
        Log.d("MainActivity", "Saved state: time=$time, isStarted=$isStarted, isPaused=$isPaused")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "Unregistering receiver")
        unregisterReceiver(updateTime)
        unregisterReceiver(notificationReceiver)
    }

    private fun createNotificationChannel() {
        val name = "Timer Notifications"
        val descriptionText = "Notifications for timer updates"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(notificationChannelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun showNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, notificationChannelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Timer Notification")
            .setContentText("Timer has reached ${getFormattedTime(time)}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(notificationId, builder.build())
        }
    }

    private fun updateClearButtonVisibility() {
        binding.rvClear.visibility = if (logAdapter.itemCount > 0) View.VISIBLE else View.GONE
    }

    private fun clearLogs() {
        logAdapter.clear()
        updateClearButtonVisibility()
    }
}
