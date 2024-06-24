package com.example.focusmate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.Activity
import android.widget.Toast
import kotlin.system.exitProcess

class QuitAppReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == ACTION_QUIT_APP) {
            // Close the app by finishing all activities
            Toast.makeText(
                context,
                "Quitting app",
                Toast.LENGTH_SHORT).show()
            exitProcess(0)
        }
    }

    companion object {
        const val ACTION_QUIT_APP = "com.example.focusmate.ACTION_QUIT_APP"
    }
}
