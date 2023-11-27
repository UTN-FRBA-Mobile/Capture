package com.example.dadmapp.data

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow

class FatalErrorHandler(
    private val userRepository: UserRepository,
    private val noteRepository: NoteRepository
) {
    private var handler: (() -> Unit)? = null

    fun setHandler(newHandler: () -> Unit) {
        handler = newHandler
    }

    suspend fun executeHandler(e: Exception? = null) {
        e.let { Log.d("INFO", it.toString()) }

        noteRepository.clear()
        userRepository.logOut()
        handler?.let { it() }
    }
}