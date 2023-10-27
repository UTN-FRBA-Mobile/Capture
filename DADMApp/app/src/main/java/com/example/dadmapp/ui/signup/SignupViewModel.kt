package com.example.dadmapp.ui.signup


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dadmapp.DADMAppApplication
import com.example.dadmapp.data.UserRepository
import kotlinx.coroutines.launch

class SignUpViewModel(userRepository: UserRepository) : ViewModel() {

    fun register(username: String, email: String, password: String, confirmPassword: String): Boolean {

        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            return false
        }

        if (password != confirmPassword) {
            return false
        }

        // Registration logic (dummy for now)
        viewModelScope.launch {

        }

        return true
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DADMAppApplication)
                val userRepository = application.container.userRepository
                SignUpViewModel(userRepository = userRepository)
            }
        }
    }
}

