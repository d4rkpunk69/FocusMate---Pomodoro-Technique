package com.example.tutorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlusMinusViewModel : ViewModel() {
    private val _result = MutableLiveData<Float>()
    val result: LiveData<Float> get() = _result

    private var res: Float = 0F

    fun addValue(value: Float) {
        res += value
        _result.value = res
    }

    fun subtractValue(value: Float) {
        res -= value
        _result.value = res
    }
}
