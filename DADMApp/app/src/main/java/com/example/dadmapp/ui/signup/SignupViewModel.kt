package com.example.dadmapp.ui.signup


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dadmapp.DADMAppApplication
import com.example.dadmapp.data.UserRepository
import com.example.dadmapp.exceptions.LoginException
import com.example.dadmapp.exceptions.SignUpException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

enum class SignupError(val title: String) {
    Fatal("Fatal"),
    UsernameExists("UsernameExists"),
    PasswordsDoNotMatch("PasswordsDoNotMatch")
}

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _registered = MutableStateFlow(false)
    val registered: StateFlow<Boolean> = _registered

    var signupError by mutableStateOf<String?>(null)

    fun register(username: String, password: String, confirmPassword: String) {

        if (password != confirmPassword) {
            signupError = SignupError.PasswordsDoNotMatch.title
            return
        }

        viewModelScope.launch {
            try {
                userRepository.signUp(username, password)
                _registered.value = true
            } catch (e: HttpException) {
                if (e.code() == 409) {
                    signupError = SignupError.UsernameExists.title
                }
                _registered.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                _registered.value = false
                signupError = SignupError.Fatal.title
            }
        }


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

