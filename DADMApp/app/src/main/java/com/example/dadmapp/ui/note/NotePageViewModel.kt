package com.example.dadmapp.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dadmapp.DADMAppApplication
import com.example.dadmapp.data.NoteRepository
import com.example.dadmapp.model.note.Note

class NotePageViewModel(private val noteRepository: NoteRepository): ViewModel() {
    suspend fun deleteNote(id: String) {
        noteRepository.deleteNote(id)
    }

    fun getNote(id: String): Note {
        return noteRepository.getNoteById(id)
    }

    suspend fun updateNote(noteId: String, title: String?, content: String?, tags: List<String>) {
        noteRepository.updateNote(noteId, title, content, tags)
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