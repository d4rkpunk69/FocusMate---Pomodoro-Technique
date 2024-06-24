package com.example.stopwatchdemo

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import java.util.*

class StopWatchService : Service() {
    private val handler = android.os.Handler()
    private var startTime = 0L
    private var time = 0.0
    private var notificationInterval = 10 * 1000 // 10 seconds

    private val timer = Timer()

    override fun onBind(p0: Intent?): IBinder? = null

    @SuppressLint("DiscouragedApi")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startTime = SystemClock.elapsedRealtime() - (time * 1000).toLong()
        timer.scheduleAtFixedRate(StopWatchTimerTask(time), 0, 1000)
        handler.post(notificationRunnable)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.d("StopWatchService", "Service destroyed")
        timer.cancel()
        handler.removeCallbacks(notificationRunnable)
        super.onDestroy()
    }

    companion object {
        const val CURRENT_TIME = "current time"
        const val UPDATED_TIME = "updated time"
        const val NOTIFICATION_TIME = "notification_time"
    }

    private inner class StopWatchTimerTask(private var time: Double) : TimerTask() {
        override fun run() {
            time++
            Log.d("StopWatchTimerTask", "Broadcasting updated time: $time")
            val intent = Intent(UPDATED_TIME)
            intent.putExtra(CURRENT_TIME, time)
            sendBroadcast(intent)
        }
    }

    private val notificationRunnable = object : Runnable {
        override fun run() {
            if (time.toInt() % 10 == 0) {
                val intent = Intent(NOTIFICATION_TIME)
                sendBroadcast(intent)
            }
            handler.postDelayed(this, 1000)
        }
    }
}
