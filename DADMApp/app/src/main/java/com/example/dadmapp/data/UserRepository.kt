package com.example.dadmapp.data

import android.util.Log
import com.example.dadmapp.model.LoginRequest
import com.example.dadmapp.network.AuthApiService
import java.lang.Exception

interface UserRepository {
    suspend fun login(username: String, password: String): Unit
}

class NetworkUserRepository(
    private val authApiService: AuthApiService,
    private val tokenInterceptor: TokenInterceptor
): UserRepository {
    override suspend fun login(username: String, password: String) {
        val body = LoginRequest(username, password)
        try {
            val res = authApiService.login(body)
            tokenInterceptor.setToken(res.token)
        } catch (e: Exception) {
            e.message?.let { Log.d("ERR", it) }
        }
    }
}