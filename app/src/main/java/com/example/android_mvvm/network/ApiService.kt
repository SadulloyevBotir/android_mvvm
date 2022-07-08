package com.example.android_mvvm.network

import com.example.android_mvvm.database.entity.User
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun getUsers(): Response<List<User>>
}