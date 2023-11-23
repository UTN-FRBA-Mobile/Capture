package com.example.dadmapp.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dadmapp.DADMAppApplication
import com.example.dadmapp.data.NoteRepository
import com.example.dadmapp.model.note.Note
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomePageViewModel(private val noteRepository: NoteRepository): ViewModel() {
    var notes: MutableStateFlow<List<Note>>? = MutableStateFlow(emptyList())
    var selectedNoteId by mutableStateOf<String?>(null)

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            notes = noteRepository.loadNotes()
        }
    }

    fun onNewNote() {
        viewModelScope.launch {
            val newNote = noteRepository.createNote()
            selectedNoteId = newNote.id.toString()
        }
    }

    fun onNewNoteFromImage(img: InputImage) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(img)
            .addOnSuccessListener { text ->
                if (img.bitmapInternal != null) {
                    viewModelScope.launch {
                        noteRepository.createNoteFromFile(img.bitmapInternal!!, text.text)
                    }
                }
            }
    }

    fun onSearchTextChanged(searchQuery: String) {
        viewModelScope.launch {
            notes = noteRepository.loadNotesWithTitleFilter(searchQuery)
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