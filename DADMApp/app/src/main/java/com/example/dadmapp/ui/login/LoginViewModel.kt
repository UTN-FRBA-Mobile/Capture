package com.example.dadmapp.ui.login

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

class LoginViewModel(private val userRepository: UserRepository): ViewModel() {
    var logged by mutableStateOf(false)

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
            userRepository.login(username, password)
            logged = true
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