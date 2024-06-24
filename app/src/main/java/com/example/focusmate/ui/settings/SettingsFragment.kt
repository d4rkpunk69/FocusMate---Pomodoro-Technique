package com.example.focusmate.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.focusmate.R
import com.example.focusmate.R.*
import com.example.focusmate.databinding.FragmentSettingsBinding
import com.example.focusmate.ui.shared.SharedViewModel

class SettingsFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesMusic: SharedPreferences
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var setHoursFocus: EditText
    private lateinit var setMinutesFocus: EditText
    private lateinit var setHoursBreak: EditText
    private lateinit var setMinutesBreak: EditText
    private lateinit var saveButton: ImageButton
    private lateinit var musicButton: ToggleButton
    private lateinit var musicIcon: ImageView
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private var isMusicOn = false

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sharedPreferences = requireActivity().getSharedPreferences(
            "timer_prefs",
            Context.MODE_PRIVATE
        )
        isPlaying = sharedPreferences.getBoolean("isPlaying", false)

        sharedPreferencesMusic = requireActivity().getSharedPreferences(
            "settings_prefs",
            Context.MODE_PRIVATE
        )
        isMusicOn = sharedPreferencesMusic.getBoolean("isMusicOn", false)
        Log.i("Set/OnCreateView", "Music Status: $isMusicOn")

        setHoursFocus = binding.setHoursFocus
        setMinutesFocus = binding.setMinutesFocus
        setHoursBreak = binding.setHoursBreak
        setMinutesBreak = binding.setMinutesBreak
        saveButton = binding.btnSave
        musicButton = binding.toggleMusic
        musicIcon = binding.musicIcon

        setHoursFocus.addTextChangedListener(textWatcher)
        setMinutesFocus.addTextChangedListener(textWatcher)
        setHoursBreak.addTextChangedListener(textWatcher)
        setMinutesBreak.addTextChangedListener(textWatcher)

        saveButton.setOnClickListener {
            if (validateInputs()) {
                val focusHours = setHoursFocus.text.toString().toIntOrNull() ?: 0
                val focusMinutes = setMinutesFocus.text.toString().toIntOrNull() ?: 0
                val breakHours = setHoursBreak.text.toString().toIntOrNull() ?: 0
                val breakMinutes = setMinutesBreak.text.toString().toIntOrNull() ?: 0

                sharedViewModel.setFocusTime(focusHours, focusMinutes)
                sharedViewModel.setBreakTime(breakHours, breakMinutes)
                Log.i("Settings Focus", "Hours: $focusHours Minutes: $focusMinutes")
                Log.i("Settings Break", "Hours: $breakHours Minutes: $breakMinutes")

                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                setHoursFocus.text.clear()
                setMinutesFocus.text.clear()
                setHoursBreak.text.clear()
                setMinutesBreak.text.clear()
            } else {
                Toast.makeText(context, "Please fill in the time correctly", Toast.LENGTH_SHORT).show()
            }
        }
        saveButton.setOnLongClickListener {
            Toast.makeText(context, "Hold detected on Save button", Toast.LENGTH_SHORT).show()
            true
        }
        if (!isMusicOn) {
            Log.i("FORCE STOP", "FORCE STOP MUSIC")
            mediaPlayer?.stop()
        }

        musicButton.setOnClickListener {
            isMusicOn = !isMusicOn
            if (isMusicOn) {
                startMusic()
                musicIcon.visibility = View.VISIBLE
                val floatingAnim = AnimationUtils.loadAnimation(context, R.drawable.floating_anim)
                musicIcon.startAnimation(floatingAnim)
            } else {
                stopMusic()
                val popAnim = AnimationUtils.loadAnimation(context, R.anim.pop_anim)
                musicIcon.startAnimation(popAnim)
                popAnim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        musicIcon.visibility = View.GONE
                    }
                    override fun onAnimationRepeat(animation: Animation?) {}
                })
            }
            updateMusicStatusUI()
            passMusicStatus()
        }
        return root
    }

    private fun updateMusicStatusUI() {
        Log.i("onCreateView UI", "Music Status: $isMusicOn")
        musicButton.isChecked = !isMusicOn
    }

    override fun onResume() {
        super.onResume()
        updateMusicStatusUI()
        Log.i("onResume", "onResume Music Status: $isMusicOn")
    }

    private fun startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, raw.focus_music)
            mediaPlayer?.isLooping = true
        }
        Log.i("MUSIC", "MUSIC PLAYING")
        mediaPlayer?.start()
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        Log.i("MUSIC", "MUSIC STOPPED")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveButtonState()
        updateMusicStatusUI()
        passMusicStatus()
        Log.i("onDest/isPlaying", isPlaying.toString())
        Log.i("onDest/Music", isMusicOn.toString())
        _binding = null
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validateInputs()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun validateInputs(): Boolean {
        val focusHours = setHoursFocus.text.toString().toIntOrNull() ?: 0
        val focusMinutes = setMinutesFocus.text.toString().toIntOrNull() ?: 0
        val breakHours = setHoursBreak.text.toString().toIntOrNull() ?: 0
        val breakMinutes = setMinutesBreak.text.toString().toIntOrNull() ?: 0
        val isFocusTimeValid = (focusHours * 60 + focusMinutes) >= 1
        val isBreakTimeValid = (breakHours * 60 + breakMinutes) >= 1
        val areAllFieldsFilled = setHoursFocus.text.isNotEmpty() &&
                setMinutesFocus.text.isNotEmpty() &&
                setHoursBreak.text.isNotEmpty() &&
                setMinutesBreak.text.isNotEmpty()

        val isValid = isFocusTimeValid && isBreakTimeValid && areAllFieldsFilled
        saveButton.isEnabled = isValid
        return isValid
    }

    private fun saveButtonState() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isPlaying", isPlaying)
        editor.apply()
    }

    private fun passMusicStatus() {
        Log.i("Settings: Passed", "Music Status: $isMusicOn")
        val editor = sharedPreferencesMusic.edit()
        editor.putBoolean("isMusicOn", isMusicOn)
        editor.apply()
    }
}
