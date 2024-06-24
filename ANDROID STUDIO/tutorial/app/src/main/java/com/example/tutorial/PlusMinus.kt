package com.example.tutorial

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer

class PlusMinus : AppCompatActivity() {

    private val plusMinusViewModel: PlusMinusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_plus_minus)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val amTo = findViewById<EditText>(R.id.etAmount)
        val plusAdd = findViewById<Button>(R.id.btnAdd)
        val minusSub = findViewById<Button>(R.id.btnSub)
        val resultView = findViewById<TextView>(R.id.viewResult)

        // Observe the result LiveData from ViewModel
        plusMinusViewModel.result.observe(this, Observer { result ->
            resultView.text = result.toString()
            Log.i("operator", result.toString())
        })

        plusAdd.setOnClickListener {
            val inputText = amTo.text.toString().toFloatOrNull()
            if (inputText != null) {
                plusMinusViewModel.addValue(inputText)
            } else {
                Toast.makeText(
                    this@PlusMinus,
                    "Please input valid number",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        minusSub.setOnClickListener {
            val inputText = amTo.text.toString().toFloatOrNull()
            if (inputText != null) {
                plusMinusViewModel.subtractValue(inputText)
            } else {
                Toast.makeText(
                    this@PlusMinus,
                    "Please input valid number",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
