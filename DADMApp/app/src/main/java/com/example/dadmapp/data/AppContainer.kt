package com.example.dadmapp.data

import com.example.dadmapp.network.AuthApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface AppContainer {
    val userRepository: UserRepository
}

class DefaultAppContainer: AppContainer {
    private val tokenInterceptor = TokenInterceptor()

    private val client = OkHttpClient.Builder()
        .addInterceptor(tokenInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .client(client)
        .build();

    private val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    override val userRepository: UserRepository by lazy {
        NetworkUserRepository(authApiService, tokenInterceptor)
    }

    companion object {
        // Por ahora hardcodeamos esto pero deberia ser config de entorno
        private const val BASE_URL = "http://10.0.2.2:3000"
    }
}