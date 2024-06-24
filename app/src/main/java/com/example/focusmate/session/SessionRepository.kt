package com.example.focusmate.session

import androidx.lifecycle.LiveData

class SessionRepository(private val sessionDao: SessionDao) {

    val allSessions: LiveData<List<Session>> = sessionDao.getAllSessions()

    suspend fun insert(session: Session) {
        sessionDao.insert(session)
    }
    suspend fun delete(session: Session) {
        sessionDao.delete(session)
    }
}
