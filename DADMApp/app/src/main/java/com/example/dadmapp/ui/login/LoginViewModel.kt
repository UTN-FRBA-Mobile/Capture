package com.example.dadmapp.ui.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dadmapp.data.UserRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.dadmapp.DADMAppApplication
import retrofit2.HttpException

class LoginViewModel(private val userRepository: UserRepository): ViewModel() {
    var logged by mutableStateOf(false)
    var loginError by mutableStateOf<String?>(null)

    init {
        isLogged()
    }

    private fun isLogged() {
        viewModelScope.launch {
            val isLogged = userRepository.hasExistingToken()
            logged = isLogged
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                userRepository.login(username, password)
                logged = true
            } catch (e: HttpException){
                Log.d("ERR", "There was an HttpException ${e.code()}")
                loginError = when(e.code()) {
                    401 -> "Invalid credentials"
                    else -> "Something went wrong"
                }
            } catch (e: Exception) {
                Log.d("ERR", "There was an Exception ${e.message}")
                loginError = "Something went wrong."
            }

        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DADMAppApplication)
                val userRepository = application.container.userRepository
                LoginViewModel(userRepository = userRepository)
            }
        }
    }
}