package com.example.focusmate

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("sessionTimestamp")
fun setSessionTimestamp(textView: TextView, timestamp: Long) {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = Date(timestamp)
    textView.text = sdf.format(date)
}
