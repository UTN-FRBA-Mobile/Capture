package com.example.dadmapp.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dadmapp.DADMAppApplication
import com.example.dadmapp.data.NoteRepository

class NotePageViewModel(private val noteRepository: NoteRepository): ViewModel() {
    suspend fun deleteNote(id: String) {
        noteRepository.deleteNote(id)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DADMAppApplication)
                val noteRepository = application.container.noteRepository
                NotePageViewModel(noteRepository)
            }
        }
    }
}