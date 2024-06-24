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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)  // Set the content view to your layout file
        val userName = intent.getStringExtra("name")
        val inPage = findViewById<TextView>(R.id.LoginNow)
        val welcoming = "Welcome Mr. $userName! Please Login."
        inPage.text = welcoming
        Log.i("mt", "Secondactivity Started $welcoming")
        val user1 = findViewById<EditText>(R.id.editTextlogin)
        val pass1 = findViewById<EditText>(R.id.editpasslogin)
        val warn = findViewById<TextView>(R.id.notice)
        val nextbut = findViewById<Button>(R.id.nextbutton)
        val clickLogin = findViewById<Button>(R.id.loginButton)
        clickLogin.setOnClickListener {
            Log.i("tag", user1.text.toString())
            Log.i("tag", pass1.text.toString())
            if (user1.text.toString() == "jose" && pass1.text.toString() == "jose143") {
                warn.visibility = INVISIBLE
                warn.text = ""
                nextbut.visibility = VISIBLE
            } else {
                Toast.makeText(
                    this@SecondActivity,
                    "Try Again",
                    Toast.LENGTH_SHORT
                ).show()
                warn.visibility = VISIBLE
                nextbut.visibility = INVISIBLE
                warn.text = getString(R.string.please_try_again)
            }
        }
        nextbut.setOnClickListener{
            val intent = Intent(this, ObjectOrientedProgramming::class.java)
            startActivity(intent)
        }

    }
    override fun onStart() {
        super.onStart()
        Log.i("LC" ,"2 onStart")
    }
    override fun onResume() {
        super.onResume()
        Log.i("LC" ,"2 onResume")
    }
    override fun onPause() {
        super.onPause()
        Log.i("LC" ,"2 onPause")
    }
    override fun onStop() {
        super.onStop()
        Log.i("LC", "2 onStop")
    }
    override fun onRestart() {
        super.onRestart()
        Log.i("LC" ,"2 onRestart")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("LC" ,"2 onDestroy")
    }
}