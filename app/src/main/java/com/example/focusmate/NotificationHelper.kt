package com.example.focusmate

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder

object NotificationHelper {
    private const val CHANNEL_ID = "session_notifications"
    private const val CHANNEL_NAME = "Session Notifications"
    private const val CHANNEL_DESCRIPTION = "Notifications for session completion"
    private const val NOTIFICATION_ID = 1
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sessionType: String

    fun createNotificationChannel(context: Context) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESCRIPTION
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("LaunchActivityFromNotification")
    fun showNotification(context: Context, title: String, message: String) {
        sharedPreferences = context.getSharedPreferences(
            "timer_prefs",
            Context.MODE_PRIVATE
        )
        sessionType = sharedPreferences.getString("sessionType", "default").toString()
        Log.i("NOTIF", sessionType)
        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
        val clickIntent = Intent(context, MainActivity::class.java)
        val clickPendingIntent = PendingIntent.getActivity(
            context,
            0,
            clickIntent,
            flag
        )
        val quitIntent = Intent(context, QuitAppReceiver::class.java).apply {
            action = QuitAppReceiver.ACTION_QUIT_APP
        }
        val quitPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            quitIntent,
            flag
        )
        val newSessionIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.timeHomeFragment) // Ensure this ID matches your navigation graph
            .setArguments(Bundle().apply {
                putBoolean("resetSession", true)
            })
            .createPendingIntent()

        val largeIcon: Bitmap? = if(sessionType == "Focus") {
            BitmapFactory.decodeResource(context.resources, R.drawable.focus_icon_png)
        } else {
            BitmapFactory.decodeResource(context.resources, R.drawable.break_icon_png)
        }

        if (largeIcon == null) {
            Log.e("NOTIF", "Large icon is null")
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setLargeIcon(largeIcon)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setPriority(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(R.drawable.icon_notif_open, "Open", clickPendingIntent)
            .addAction(R.drawable.icon_notification, "New Session", newSessionIntent)
            .addAction(R.drawable.icon_notif_close, "Quit App", quitPendingIntent)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
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
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}
