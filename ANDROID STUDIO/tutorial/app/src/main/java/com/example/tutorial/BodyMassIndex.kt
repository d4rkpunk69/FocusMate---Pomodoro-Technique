package com.example.tutorial

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BodyMassIndex : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_body_mass_index)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val weight = findViewById<EditText>(R.id.weightKg)
        val height = findViewById<EditText>(R.id.heightFt)
        var result = 0;
        val calculate = findViewById<Button>(R.id.calcButton)
        calculate.setOnClickListener {
            val w = weight.text.toString()
            Log.i("bmi", w.toFloat().toString())
            val h = height.text.toString()
            Log.i("bmi", h.toFloat().toString())
            val bmi = (w.toFloat() / ((h.toFloat() / 100) * (h.toFloat() / 100)))
            Log.i("bmi", bmi.toString().toString())
            val bmi2dig = String.format("%.2f", bmi).toFloat()
            Log.i("bmi", bmi2dig.toFloat().toString().toString())
            displayResult(bmi2dig.toFloat().toString().toString())
            verifyStat(bmi2dig)
        }
    }
    private fun displayResult(bmi:String) {
        val resNum =  findViewById<TextView>(R.id.ResultNum)
        val info = findViewById<TextView>(R.id.inform)
        resNum.text = bmi.toFloat().toString()
    }
    private fun verifyStat(bmi:Float) {
        val resWor = findViewById<TextView>(R.id.ResultWord)
        var resWord = when {
            bmi < 18.5 -> "Underweight"
            bmi in 18.5..24.9 -> "Normal weight"
            bmi in 25.0..29.9 -> "Overweight"
            bmi in 30.0..34.9 -> "Obesity Class 1"
            bmi in 35.0..39.9 -> "Obesity Class 2"
            else -> "Obesity Class 3"
        }
        resWor.text = resWord
    }
}