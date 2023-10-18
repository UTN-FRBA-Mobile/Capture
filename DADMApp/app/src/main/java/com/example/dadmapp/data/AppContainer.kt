package com.example.dadmapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.dadmapp.LOCALHOST_URL
import com.example.dadmapp.network.AuthApiService
import com.example.dadmapp.network.NoteApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit


interface AppContainer {
    val userRepository: UserRepository
    val noteRepository: NoteRepository
}

class DefaultAppContainer(dataStore: DataStore<Preferences>): AppContainer {
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

    private val noteApiService: NoteApiService by lazy {
        retrofit.create(NoteApiService::class.java)
    }

    override val userRepository: UserRepository by lazy {
        NetworkUserRepository(authApiService, tokenInterceptor, dataStore)
    }

    override val noteRepository: NoteRepository by lazy {
        NetworkNoteRepository(noteApiService)
    }

    companion object {
        // Por ahora hardcodeamos esto pero deberia ser config de entorno
        private const val BASE_URL = LOCALHOST_URL
    }
}