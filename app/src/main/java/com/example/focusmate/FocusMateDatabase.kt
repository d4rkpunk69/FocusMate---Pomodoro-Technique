package com.example.focusmate

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.focusmate.session.Session
import com.example.focusmate.session.SessionDao

@Database(entities = [Session::class], version = 1, exportSchema = false)
abstract class FocusMateDatabase : RoomDatabase() {

    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: FocusMateDatabase? = null

        fun getDatabase(context: Context): FocusMateDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FocusMateDatabase::class.java,
                    "focusmate_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
