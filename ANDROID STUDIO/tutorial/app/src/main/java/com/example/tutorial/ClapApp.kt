package com.example.tutorial

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClapApp : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_clap_app)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.i("clap", "override onCreate")
        seekBar = findViewById(R.id.seekBar)
        handler = Handler(Looper.getMainLooper())
        val playBut = findViewById<FloatingActionButton>(R.id.playButton)
        playBut.setOnClickListener{
            if(mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.clap1)
                initializeSeekbar()
            }
            mediaPlayer?.start()
        }

        val pauseBut = findViewById<FloatingActionButton>(R.id.pauseButton)
        pauseBut.setOnClickListener{
            mediaPlayer?.pause()
        }

        val stopBut = findViewById<FloatingActionButton>(R.id.stopButton)
        stopBut.setOnClickListener{
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
            seekBar.progress = 0
        }

        val btnDownload = findViewById<Button>(R.id.btnDownload)
        btnDownload.setOnClickListener {
            lifecycleScope.launch {
                downloadHere()
            }
        }
    }

    private fun initializeSeekbar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        seekBar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekBar.progress = mediaPlayer!!.currentPosition
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    private suspend fun downloadHere() {
        val tvDisplay = findViewById<TextView>(R.id.tvDownloading)
        for (i in 1..10000) {
            Log.i("Mytag", "Downloading $i in ${Thread.currentThread().name}")
            withContext(Dispatchers.Main) {
                tvDisplay.text = "Downloading user data in $i"
            }
            delay(250)
        }
    }
}
