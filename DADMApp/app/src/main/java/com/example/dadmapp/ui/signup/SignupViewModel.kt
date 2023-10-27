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

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registered = MutableStateFlow(false)
    val registered: StateFlow<Boolean> = _registered

    var registeredError by mutableStateOf<String?>(null)

    fun register(username: String, password: String, confirmPassword: String) {

        if (password != confirmPassword) {
            registeredError = "Passwords do not match!"
            return
        }

        viewModelScope.launch {
            try {
                userRepository.signUp(username, password) // This can throw a SignUpException
                userRepository.login(username, password)  // This can throw a LoginException
                _registered.value = true
            } catch (e: SignUpException) {
                // Handle sign-up specific exception
                registeredError = "Issue during registration. Please try again."
                _registered.value = false
            } catch (e: LoginException) {
                // Handle login specific exception during the sign-up process
                registeredError = "Automatic login after registration failed. Please try logging in manually."
                _registered.value = false
            } catch (e: HttpException) {
                // Handle specific HTTP error codes
                registeredError = when(e.code()) {
                    409 -> "Username already exists"
                    else -> "Something went wrong"
                }
                _registered.value = false
            } catch (e: Exception) {
                // Handle other general exceptions
                Log.d("ERR", "There was an Exception ${e.message}")
                e.printStackTrace()
                registeredError = "Unexpected error occurred. Please try again."
                _registered.value = false
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

