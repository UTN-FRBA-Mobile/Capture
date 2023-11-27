package com.example.dadmapp.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.dadmapp.exceptions.LoginException
import com.example.dadmapp.exceptions.SignUpException
import com.example.dadmapp.model.login.LoginRequest
import com.example.dadmapp.model.singup.SignupRequest
import com.example.dadmapp.network.AuthApiService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

interface UserRepository {
    suspend fun login(username: String, password: String)
    suspend fun signUp(username: String, password: String)
    suspend fun hasExistingToken(): Boolean
    suspend fun logOut()
    suspend fun getUsername(): String
}

class NetworkUserRepository(
    private val authApiService: AuthApiService,
    private val tokenInterceptor: TokenInterceptor,
    private val dataStore: DataStore<Preferences>
): UserRepository {
    private val TOKEN_KEY = stringPreferencesKey(TOKEN_NAME)
    private val USER_KEY = stringPreferencesKey(USER_NAME)

    override suspend fun logOut() {
        dataStore.edit {
            it.remove(TOKEN_KEY)
            it.remove(USER_KEY)
        }
        tokenInterceptor.setToken(null)
    }

    override suspend fun login(username: String, password: String) {
        val body = LoginRequest(username, password)
        try {
            val res = authApiService.login(body)
            tokenInterceptor.setToken(res.token)
            dataStore.edit { settings ->
                settings[TOKEN_KEY] = res.token
                settings[USER_KEY] = username
            }
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> throw LoginException("Invalid credentials.")
                403 -> throw LoginException("Account not activated.")
                else -> throw LoginException("Something went wrong during login.")
            }
        }
    }

    override suspend fun signUp(username: String, password: String) {
        val body = SignupRequest(username, password)
        authApiService.signUp(body)
    }


    override suspend fun hasExistingToken(): Boolean {
        val token = dataStore.data.map {
                preferences -> preferences[TOKEN_KEY]
        }.firstOrNull()

        if (token != null) {
            try {
                tokenInterceptor.setToken(token)
                authApiService.valid()
                return true
            } catch (e: Exception) {
                Log.d("INFO", "User not authorized")
                tokenInterceptor.setToken(null)
                return false
            }
        }

        return false
    }

    override suspend fun getUsername(): String {
        val username = dataStore.data.map {
            preferences -> preferences[USER_KEY]
        }.firstOrNull()

        return username ?: ""
    }



    companion object {
        private const val TOKEN_NAME = "TOKEN"
        private const val USER_NAME = "USER"
    }
}