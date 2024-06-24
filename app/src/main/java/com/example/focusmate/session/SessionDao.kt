package com.example.focusmate.session

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SessionDao {
    @Insert
    suspend fun insert(session: Session)

    @Delete
    suspend fun delete(session: Session)

    @Query("SELECT * FROM session_table ORDER BY timestamp DESC")
    fun getAllSessions(): LiveData<List<Session>>
}
