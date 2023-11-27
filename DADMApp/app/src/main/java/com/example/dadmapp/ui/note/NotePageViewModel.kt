package com.example.dadmapp.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dadmapp.DADMAppApplication
import com.example.dadmapp.data.FatalErrorHandler
import com.example.dadmapp.data.NoteRepository
import com.example.dadmapp.model.note.Note
import com.example.dadmapp.model.tag.Tag

class NotePageViewModel(
    private val noteRepository: NoteRepository,
    private val fatalErrorHandler: FatalErrorHandler
): ViewModel() {
    suspend fun deleteNote(id: String) {
        try {
            noteRepository.deleteNote(id)
        } catch (e: Exception) {
            fatalErrorHandler.executeHandler(e)
        }
    }

    fun getNote(id: String): Note {
        return noteRepository.getNoteById(id)
    }

    suspend fun updateNote(noteId: String, title: String?, content: String?, tags: List<Tag>) {
        try {
            noteRepository.updateNote(noteId, title, content, tags)
        } catch (e: Exception) {
            fatalErrorHandler.executeHandler(e)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DADMAppApplication)
                val noteRepository = application.container.noteRepository
                val fatalErrorHandler = application.container.fatalErrorHandler
                NotePageViewModel(noteRepository, fatalErrorHandler)
            }
        }
    }
}