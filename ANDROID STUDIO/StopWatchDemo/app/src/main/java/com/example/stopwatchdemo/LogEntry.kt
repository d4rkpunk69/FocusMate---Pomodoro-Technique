package com.example.stopwatchdemo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class LogEntry(val action: String, val timerDisplay: String, val timestamp: String)

class LogAdapter : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {
    private val logEntries = mutableListOf<LogEntry>()

    fun addLogEntry(logEntry: LogEntry) {
        logEntries.add(logEntry)
        notifyItemInserted(logEntries.size - 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        logEntries.clear()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_log_entry, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.bind(logEntries[position])
    }

    override fun getItemCount() = logEntries.size

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actionTextView: TextView = itemView.findViewById(R.id.tvAction)
        private val timestampTextView: TextView = itemView.findViewById(R.id.tvTimestamp)
        private val timerDisplayTextView: TextView = itemView.findViewById(R.id.tvTimerDisplay)

        fun bind(logEntry: LogEntry) {
            actionTextView.text = logEntry.action
            timestampTextView.text = logEntry.timestamp
            timerDisplayTextView.text = logEntry.timerDisplay
        }
    }
}
