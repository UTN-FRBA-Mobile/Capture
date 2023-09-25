package com.example.dadmapp.network

import com.example.dadmapp.model.LoginRequest
import com.example.dadmapp.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse
}