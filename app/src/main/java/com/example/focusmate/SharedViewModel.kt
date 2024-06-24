package com.example.focusmate.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _focusTime = MutableLiveData<Long>()
    val focusTime: LiveData<Long> = _focusTime

    private val _breakTime = MutableLiveData<Long>()
    val breakTime: LiveData<Long> = _breakTime

    fun setFocusTime(hours: Int, minutes: Int) {
        _focusTime.value = (hours * 60 + minutes) * 60 * 1000L
    }

    fun setBreakTime(hours: Int, minutes: Int) {
        _breakTime.value = (hours * 60 + minutes) * 60 * 1000L
    }

    fun reset(){
//        _focusTime.value = 0
//        _breakTime.value = 0
    }
}
