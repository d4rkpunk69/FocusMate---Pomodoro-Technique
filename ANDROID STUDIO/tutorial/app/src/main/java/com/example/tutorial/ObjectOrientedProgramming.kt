package com.example.tutorial

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ObjectOrientedProgramming : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_oriented_programming)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set up the drawer layout
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        // Set up the drawer toggle
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle navigation item selection
        val boolT = true
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Handle Home click
                    boolT
                }
                R.id.clapApp -> {
                    startActivity(Intent(this, ClapApp::class.java))
                    boolT
                }
                R.id.BMI -> {

                    startActivity(Intent(this, BodyMassIndex::class.java))
                    boolT
                }
                R.id.SPD -> {
                    startActivity(Intent(this, SPDemo::class.java))
                    boolT
                }
                R.id.manipulateButton -> {
                    startActivity(Intent(this, PlusMinus::class.java))
                    boolT
                }
                R.id.recyclerView -> {
                    startActivity(Intent(this, RecyclerView::class.java))
                    boolT
                }
                else -> {
                    false
                }
            }.also {
                drawerLayout.closeDrawers()
            }
        }
    }
}
