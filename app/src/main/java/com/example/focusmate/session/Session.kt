package com.example.focusmate.session

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_table")
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionType: String,
    val durationInMillis: Long,
    val timestamp: Long = System.currentTimeMillis()
)
