package com.example.studentregistration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentregistration.Model.User
import com.example.studentregistration.Model.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel : ViewModel() {
private var userRepository = UserRepository()
    var users :MutableLiveData<List<User>?> = MutableLiveData()
    fun getUserData() {
        viewModelScope.launch {
        var result : List<User>? = null
            withContext(Dispatchers.IO) {
                result = userRepository.getUsers()
            }
            users.value = result
        }
    }
}