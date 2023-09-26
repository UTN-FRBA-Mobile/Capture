package com.example.dadmapp.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.dadmapp.model.login.LoginRequest
import com.example.dadmapp.network.AuthApiService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.lang.Exception

interface UserRepository {
    suspend fun login(username: String, password: String)
    suspend fun hasExistingToken(): Boolean
}

data class LoggedValue(var logged: Boolean)

class NetworkUserRepository(
    private val authApiService: AuthApiService,
    private val tokenInterceptor: TokenInterceptor,
    private val dataStore: DataStore<Preferences>
): UserRepository {
    private val TOKEN_KEY = stringPreferencesKey(TOKEN_NAME)

    override suspend fun login(username: String, password: String) {
        val body = LoginRequest(username, password)
        try {
            val res = authApiService.login(body)
            tokenInterceptor.setToken(res.token)

            dataStore.edit { settings ->
                settings[TOKEN_KEY] = res.token
            }
        } catch (e: Exception) {
            e.message?.let { Log.d("ERR", it) }
        }
    }

    override suspend fun hasExistingToken(): Boolean {
        val token = dataStore.data.map {
                preferences -> preferences[TOKEN_KEY]
        }.firstOrNull()

        if (token != null) {
            tokenInterceptor.setToken(token)
            return true
        }

        return false
    }

    companion object {
        private const val TOKEN_NAME = "TOKEN"
    }
}