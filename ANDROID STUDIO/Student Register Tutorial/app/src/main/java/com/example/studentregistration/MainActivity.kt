package com.example.studentregistration

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.studentregistration.Model.UserRepository

class MainActivity : AppCompatActivity() {
    private lateinit var mainActivityViewModel : MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mainActivityViewModel=ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel.getUserData()
        mainActivityViewModel.users.observe(this, Observer {
            myUsers ->
            if (myUsers != null) {
                myUsers.forEach{
                    Log.i("Tag", "name is ${it.name} & id is ${it.id}")
                }
            }
        })
    }
}