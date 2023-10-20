package com.example.dadmapp.ui.recordAudio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dadmapp.DADMAppApplication
import com.example.dadmapp.data.NoteRepository
import kotlinx.coroutines.launch
import java.io.File

class RecordPageViewModel(private val noteRepository: NoteRepository): ViewModel() {
    fun onNewNoteFromAudio(file: File) {
        viewModelScope.launch {
            noteRepository.createNoteFromAudio(file, "")
            file.delete()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DADMAppApplication)
                val noteRepository = application.container.noteRepository
                RecordPageViewModel(noteRepository)
            }
        }
    }
}