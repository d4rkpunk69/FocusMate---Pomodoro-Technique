package com.example.focusmatenav.menu

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.focusmatenav.R

class SettingsMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings_menu)
        setSupportActionBar(findViewById(R.id.toolbar))
        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle the Up button press event using OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Finish the activity when back button is pressed
                finish()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        // Handle the Up button press event
        finish()
        return true
    }
}
