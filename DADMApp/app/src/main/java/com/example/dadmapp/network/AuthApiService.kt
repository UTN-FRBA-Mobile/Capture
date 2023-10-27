package com.example.dadmapp.network

import com.example.dadmapp.model.login.LoginRequest
import com.example.dadmapp.model.login.LoginResponse
import com.example.dadmapp.model.singup.SignupRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @POST("auth/valid")
    suspend fun valid()

    @POST("user/create")
    suspend fun register(@Body body: SignupRequest): LoginResponse
}