package com.example.focusmate

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.focusmate.databinding.ActivityMainBinding
import com.example.focusmate.ui.shared.SharedViewModel
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private val REQUEST_NOTIFICATION_PERMISSION = 1
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var isNotificationEnabled = false

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            //Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            isNotificationEnabled = true
            Log.i("Main/requestNotificationPermissionIF: ", isNotificationEnabled.toString())
            passData()
        } else {
            //Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            isNotificationEnabled = false
            Log.i("Main/requestNotificationPermissionELSE: ", isNotificationEnabled.toString())
            passData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        resetApp()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        NotificationHelper.createNotificationChannel(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        sharedPreferences = this.getSharedPreferences(
            "isNotificationEnabled",
            Context.MODE_PRIVATE
        )
        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.timeHomeFragment, R.id.settings_fragment, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Create the notification channel for Android 8.0 and above
        NotificationHelper.createNotificationChannel(this)

        // Request notification permission for Android 13 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        handleIntent(intent)
    }
//
private fun handleIntent(intent: Intent) {
    intent.data?.let { uri ->
        if (uri.scheme == "focusmate" && uri.host == "time_home") {
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.timeHomeFragment)
        }
    }
}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                NotificationHelper.createNotificationChannel(this)
            } else {
                // Permission denied, show some message or handle accordingly
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun resetApp() {
        // Clear SharedPreferences
        val sharedPreferences = getSharedPreferences("timer_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Reset ViewModel or Data Repository
        val sharedViewModel: SharedViewModel by viewModels()
        sharedViewModel.reset()

        // Handle any other initialization/reset tasks
        // Example: Reset UI components if you have any reference
    }

    private fun passData() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isNotificationEnabled", this.isNotificationEnabled)
        editor.apply()
        Log.i("Main/passData: ", this.isNotificationEnabled.toString())
    }
}