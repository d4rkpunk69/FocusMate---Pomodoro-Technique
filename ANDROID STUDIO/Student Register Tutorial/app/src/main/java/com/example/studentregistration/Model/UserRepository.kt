package com.example.studentregistration.Model

import kotlinx.coroutines.delay

class UserRepository {

    suspend fun getUsers() : List<User> {
        delay(1000)
        val users : List<User> = listOf(
            User(1, "Jose"),
            User(2, "Geraldine"),
            User(3, "Maxie")
        )
        return users
    }
}