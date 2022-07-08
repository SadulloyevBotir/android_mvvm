package com.example.android_mvvm.repositories

import com.example.android_mvvm.database.AppDatabase
import com.example.android_mvvm.database.entity.User
import com.example.android_mvvm.network.ApiService

class UserRepository(private val apiService: ApiService, private val appDatabase: AppDatabase) {

    suspend fun getRemoteUsers() = apiService.getUsers()

    suspend fun getLocalUsers() = appDatabase.userDao().getAllUsers()

    suspend fun addUsers(users : List<User>) = appDatabase.userDao().addUsers(users)
}