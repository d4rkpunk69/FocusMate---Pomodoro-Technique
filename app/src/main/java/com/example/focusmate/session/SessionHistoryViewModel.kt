package com.example.focusmate.session

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.focusmate.FocusMateDatabase
import kotlinx.coroutines.launch

class SessionHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SessionRepository
    val allSessions: LiveData<List<Session>>

    init {
        val sessionDao = FocusMateDatabase.getDatabase(application).sessionDao()
        repository = SessionRepository(sessionDao)
        allSessions = repository.allSessions
    }

    fun delete(session: Session) = viewModelScope.launch {
        repository.delete(session)
    }

    fun insert(session: Session) = viewModelScope.launch {
        repository.insert(session)
    }
}

class SessionHistoryViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SessionHistoryViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
