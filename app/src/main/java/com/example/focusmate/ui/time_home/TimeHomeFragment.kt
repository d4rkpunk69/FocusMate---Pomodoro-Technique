package com.example.focusmate.ui.time_home

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.example.focusmate.R
import com.example.focusmate.session.Session
import com.example.focusmate.session.SessionHistoryViewModel
import com.example.focusmate.session.SessionHistoryViewModelFactory
import com.example.focusmate.databinding.FragmentTimeHomeBinding
import com.example.focusmate.ui.shared.SharedViewModel
import com.example.focusmate.NotificationHelper

class TimeHomeFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesMusic: SharedPreferences
    private lateinit var sharedPreferencesNotif: SharedPreferences
    private lateinit var clockText: TextView
    private lateinit var announceText: TextView
    private lateinit var wifiBtn: ImageButton
    private lateinit var sndBtn: ImageButton
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnHistory: Button
    private lateinit var btnSet : Button
    private lateinit var notifBtn: ImageButton
    private var isPlaying = false
    private var isMusicOn = false
    private var currentTimer: CountDownTimer? = null
    private var isFocusSession = true
    private var remainingTimeInMillis: Long = 0
    private var isNotificationEnabled = false

    private var _binding: FragmentTimeHomeBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val sessionHistoryViewModel: SessionHistoryViewModel by activityViewModels {
        SessionHistoryViewModelFactory(requireActivity().application)
    }

    private val wifiReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateWifiStatus()
        }
    }
    private val ringerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateRingerStatus()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimeHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        activity?.title = getString(R.string.history)
        sharedPreferences = requireActivity().getSharedPreferences(
            "timer_prefs",
            Context.MODE_PRIVATE
        )
        sharedPreferencesMusic = requireActivity().getSharedPreferences(
            "settings_prefs",
            Context.MODE_PRIVATE
        )
        sharedPreferencesNotif = requireActivity().getSharedPreferences(
            "isNotificationEnabled",
            Context.MODE_PRIVATE
        )
        isNotificationEnabled = sharedPreferencesNotif.getBoolean("isNotificationEnabled", false)
        Log.i("Home: Received: ", "Notification Status: $isNotificationEnabled")

        sndBtn = binding.imgbtnSnd
        wifiBtn = binding.imgbtnWifi
        clockText = binding.tvTimeHome
        announceText = binding.tvSessionAnnouncer
        btnPlayPause = binding.btnPlayPause
        btnHistory = binding.btnHistory
        btnPlayPause.isEnabled = false
        clockText.text = getString(R.string.time_default)
        btnSet = binding.gotoSet
        notifBtn = binding.imgbtnNotif
        btnSet.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_settings)
        }

        // Observe focus time and break time changes
        sharedViewModel.focusTime.observe(viewLifecycleOwner) { focusTime ->
            focusTime?.let {
                if (isFocusSession) {
                    updateSession(it, "Focus")
                }
                btnSet.visibility = View.GONE
            } ?: run {
                clockText.text = getString(R.string.time_default)
                announceText.text = getText(R.string.please_set_time_first)
                btnPlayPause.isEnabled = false
                btnSet.visibility = View.VISIBLE
            }
        }

        sharedViewModel.breakTime.observe(viewLifecycleOwner) { breakTime ->
            breakTime?.let {
                if (!isFocusSession) {
                    updateSession(it, "Break")
                }
            } ?: run {
                clockText.text = getString(R.string.time_default)
                announceText.text = getText(R.string.please_set_time_first)
                btnPlayPause.isEnabled = false
            }
        }

        btnPlayPause.setOnClickListener {
            if (isPlaying) {
                pauseTimer()
            } else {
                startTimer()
            }
        }
        btnHistory.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_sessionHistoryFragment)
        }

        updateWifiStatus()
        updateRingerStatus()
        updateNotificationStatus()


        Log.i("Home: Received: ", "Music Status: " + sharedPreferencesMusic.getBoolean("isMusicOn", false).toString())

        isPlaying = sharedPreferences.getBoolean("isPlaying", false)
        btnPlayPause.setImageResource(if (isPlaying) R.drawable.icon_pause else R.drawable.icon_play)

        return root
    }

    private fun updateNotificationStatus() {
        if (isNotificationEnabled) {
            notifBtn.setImageResource(R.drawable.notifienabled_icon)
        } else {
            notifBtn.setImageResource(R.drawable.notifdisabled_icon)
        }
    }


    @SuppressLint("DefaultLocale")
    private fun updateSession(timeInMillis: Long, sessionType: String) {
        val hours = timeInMillis / 1000 / 60 / 60
        val minutes = (timeInMillis / 1000 / 60) % 60
        val seconds = (timeInMillis / 1000) % 60

        // Format the time display
        val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        clockText.text = formattedTime

        // Determine the session type and update announcement text
        val nextSessionText = if (sessionType == "Focus") {
            val breakTime = sharedViewModel.breakTime.value ?: 0L
            val breakHours = breakTime / 1000 / 60 / 60
            val breakMinutes = (breakTime / 1000 / 60) % 60

            // Construct announcement text for Focus session
            val breakTimeText = getMinutesString(breakTime)
            val breakTimeFormatted = if (breakHours > 0) "$breakHours ${if (breakHours > 1) "hrs" else "hr"} " else ""
            "Next Session: ${breakTimeFormatted}${breakTimeText} Break"
        } else {
            val focusTime = sharedViewModel.focusTime.value ?: 0L
            val focusHours = focusTime / 1000 / 60 / 60
            val focusMinutes = (focusTime / 1000 / 60) % 60

            // Construct announcement text for Break session
            val focusTimeText = getMinutesString(focusTime)
            val focusTimeFormatted = if (focusHours > 0) "$focusHours ${if (focusHours > 1) "hrs" else "hr"} " else ""
            "Next Session: ${focusTimeFormatted}${focusTimeText} Focus"
        }

        announceText.text = nextSessionText
        btnPlayPause.isEnabled = true
    }

    private fun getMinutesString(timeInMillis: Long): String {
        val minutes = (timeInMillis / 1000 / 60) % 60
        return "$minutes min"
    }


    private fun startTimer() {
        isPlaying = true
        btnPlayPause.setImageResource(R.drawable.icon_pause)
        saveButtonState()
        if (remainingTimeInMillis > 0) {
            startCountDown(remainingTimeInMillis)
        } else {
            if (isFocusSession) {
                sharedViewModel.focusTime.value?.let { startCountDown(it) }
            } else {
                sharedViewModel.breakTime.value?.let { startCountDown(it) }
            }
        }
    }

    private fun pauseTimer() {
        isPlaying = false
        btnPlayPause.setImageResource(R.drawable.icon_play)
        saveButtonState()
        currentTimer?.cancel()
    }

    private fun startCountDown(timeInMillis: Long) {
        remainingTimeInMillis = timeInMillis
        currentTimer = object : CountDownTimer(timeInMillis, 1000) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeInMillis = millisUntilFinished
                val hours = millisUntilFinished / 1000 / 60 / 60
                val minutes = (millisUntilFinished / 1000 / 60) % 60
                val seconds = (millisUntilFinished / 1000) % 60
                clockText.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }

            override fun onFinish() {
                remainingTimeInMillis = 0
                // Save the session to the database
                val sessionType = if (isFocusSession) "Focus" else "Break"
                sharedPreferences.edit().putString("sessionType", sessionType).apply()
                val duration = if (isFocusSession) sharedViewModel.focusTime.value ?: 0L else sharedViewModel.breakTime.value ?: 0L
                val session = Session(sessionType = sessionType, durationInMillis = duration, timestamp = System.currentTimeMillis())
                sessionHistoryViewModel.insert(session)

                // Toggle the session type
                isFocusSession = !isFocusSession
                if (isFocusSession) {
                    updateSession(sharedViewModel.focusTime.value ?: 0L, "Focus")
                } else {
                    updateSession(sharedViewModel.breakTime.value ?: 0L, "Break")
                }
                startTimer()

                NotificationHelper.showNotification(
                    requireContext(),
                    sessionType + " " + getString(R.string.session_complete),
                    "Congratulations! You have completed your $sessionType session."
                )
                Toast.makeText(context, sessionType + " session completed!", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }


    private fun recordSession(durationInMillis: Long) {
        val sessionType = "Focus"  // Or get this from user input or session settings
        val session = Session(sessionType = sessionType, durationInMillis = durationInMillis)
        sessionHistoryViewModel.insert(session)
        Toast.makeText(context, "Session recorded!", Toast.LENGTH_SHORT).show()
    }

    private fun updateWifiStatus() {
        val wifiManager = requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifiManager.isWifiEnabled) {
            wifiBtn.setImageResource(R.drawable.icon_wifi_on)
        } else {
            wifiBtn.setImageResource(R.drawable.icon_wifi_off)
        }
    }

    private fun updateRingerStatus() {
        val audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> {
                sndBtn.setImageResource(R.drawable.icon_ringer_normal)
            }
            AudioManager.RINGER_MODE_VIBRATE -> {
                sndBtn.setImageResource(R.drawable.icon_ringer_vibrate)
            }
            AudioManager.RINGER_MODE_SILENT -> {
                sndBtn.setImageResource(R.drawable.icon_ringer_silent)
            }
        }
    }

    private fun saveButtonState() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isPlaying", isPlaying)
        editor.putLong("remainingTimeInMillis", remainingTimeInMillis)
        editor.putBoolean("isFocusSession", isFocusSession)
        editor.apply()
    }

    override fun onStart() {
        super.onStart()
        requireContext().registerReceiver(wifiReceiver, IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION))
        requireContext().registerReceiver(ringerReceiver, IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION))
    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(wifiReceiver)
        requireContext().unregisterReceiver(ringerReceiver)
        saveButtonState()
    }

    override fun onPause() {
        super.onPause()
        saveButtonState()
    }

    override fun onResume() {
        super.onResume()
        isPlaying = sharedPreferences.getBoolean("isPlaying", false)
        remainingTimeInMillis = sharedPreferences.getLong("remainingTimeInMillis", 0L)
        isFocusSession = sharedPreferences.getBoolean("isFocusSession", true)
        btnPlayPause.setImageResource(if (isPlaying) R.drawable.icon_pause else R.drawable.icon_play)
       // Log.i("onResume/Home: Received: ", "Music Status: " + sharedPreferencesMusic.getBoolean("isMusicOn", false).toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val editor = sharedPreferencesMusic.edit()
        editor.putBoolean("isMusicOn", isMusicOn)
        editor.apply()
        _binding = null
    }
}
