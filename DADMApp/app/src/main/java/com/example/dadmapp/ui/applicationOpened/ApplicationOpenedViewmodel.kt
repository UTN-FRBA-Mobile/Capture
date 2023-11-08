package com.example.dadmapp.ui.applicationOpened

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dadmapp.DADMAppApplication
import com.example.dadmapp.data.NoteRepository
import com.example.dadmapp.data.UserRepository
import retrofit2.HttpException

enum class LoginState(val stateName: String) {
    NOT_LOADED("NOT_LOADED"),
    FAILURE("FAILURE"),
    LOGGED("LOGGED"),
    NOT_LOGGED_BEFORE("NOT_LOGGED_BEFORE")
}

class ApplicationOpenedViewmodel(
    private val userRepository: UserRepository,
    private val noteRepository: NoteRepository
): ViewModel() {
    var loginState by mutableStateOf(LoginState.NOT_LOADED.stateName)

    suspend fun tryToLoadNotes() {
        if (!userRepository.hasExistingToken()) {
            loginState = LoginState.NOT_LOGGED_BEFORE.stateName
            return
        }

        try {
            noteRepository.loadNotes()
            loginState = LoginState.LOGGED.stateName
        } catch (e: HttpException) {
            loginState = LoginState.FAILURE.stateName
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DADMAppApplication)
                val noteRepository = application.container.noteRepository
                val userRepository = application.container.userRepository
                ApplicationOpenedViewmodel(
                    noteRepository = noteRepository,
                    userRepository = userRepository
                )
            }
        }
    }
}