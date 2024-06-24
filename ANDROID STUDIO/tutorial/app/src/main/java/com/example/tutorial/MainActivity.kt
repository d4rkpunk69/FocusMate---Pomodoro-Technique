package com.example.tutorial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("mt", "ONCREATE")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val welcomeText = findViewById<TextView>(R.id.welcome)
        val inputText = findViewById<EditText>(R.id.editTextField)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val continueButton = findViewById<Button>(R.id.continueButton)
        var enteredText = ""
        submitButton.setOnClickListener {
            enteredText = inputText.text.toString()
            Log.i("mt", enteredText)
            if (enteredText == "") {
                welcomeText.text = ""
                Toast.makeText(
                    this@MainActivity,
                    "Please input you name!",
                    Toast.LENGTH_LONG
                ).show()
                continueButton.visibility = INVISIBLE
                Log.i("mt", "no input")
            } else {
                continueButton.visibility = VISIBLE
                val welcomeMessage = getString(R.string.welcome, inputText.text)
                welcomeText.text = welcomeMessage
                inputText.text.clear()
                Log.i("mt", "Name: $inputText")
            }
        }
        continueButton.setOnClickListener {
            Log.i("mt", "Passed: $enteredText")
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("name", enteredText)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        Log.i("LC" ,"onStart")
    }
    override fun onResume() {
        super.onResume()
        Log.i("LC" ,"onResume")
    }
    override fun onPause() {
        super.onPause()
        Log.i("LC" ,"onPause")
    }
    override fun onStop() {
        super.onStop()
        Log.i("LC", "onStop")
    }
    override fun onRestart() {
        super.onRestart()
        Log.i("LC" ,"onRestart")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("LC" ,"onDestroy")
    }
}