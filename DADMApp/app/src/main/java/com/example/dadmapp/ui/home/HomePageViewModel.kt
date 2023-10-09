package com.example.dadmapp.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dadmapp.data.NoteRepository
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import com.example.dadmapp.DADMAppApplication
import com.example.dadmapp.model.note.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomePageViewModel(private val noteRepository: NoteRepository): ViewModel() {
    var notesFlow: MutableStateFlow<List<Note>>? = MutableStateFlow(emptyList())
    var selectedNoteId by mutableStateOf<String?>(null)

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            notesFlow = noteRepository.loadNotes()
        }
    }

    fun onNewNote() {
        viewModelScope.launch {
            val newNote = noteRepository.createNote()
            selectedNoteId = newNote.id.toString()
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DADMAppApplication)
                val noteRepository = application.container.noteRepository
                HomePageViewModel(noteRepository)
            }
        }
    }
}