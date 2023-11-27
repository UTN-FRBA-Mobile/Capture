package com.example.dadmapp.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dadmapp.data.NoteRepository
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import com.example.dadmapp.DADMAppApplication
import com.example.dadmapp.data.FatalErrorHandler
import com.example.dadmapp.data.UserRepository
import com.example.dadmapp.model.note.Note
import com.example.dadmapp.model.tag.Tag
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomePageViewModel(
    private val noteRepository: NoteRepository,
    private val userRepository: UserRepository,
    private val fatalErrorHandler: FatalErrorHandler
): ViewModel() {
    var notes: MutableStateFlow<List<Note>>? = MutableStateFlow(emptyList())
    var tags: MutableStateFlow<List<Tag>>? = MutableStateFlow(emptyList())
    var selectedNoteId by mutableStateOf<String?>(null)
    var filterByTags: List<Tag> by mutableStateOf(emptyList())
    var searchTerm by mutableStateOf<String?>(null)
    var username by mutableStateOf<String?>(null)

    init {
        loadNotes()
        viewModelScope.launch {
            username = userRepository.getUsername()
        }
    }

    fun onLogOut(otherLogoutActions: () -> Unit) {
        viewModelScope.launch {
            userRepository.logOut()
            otherLogoutActions()
            noteRepository.clear()
        }
    }

    private fun loadNotes() {
        viewModelScope.launch {
            try {
                notes = noteRepository.loadNotes()
                tags = noteRepository.getTags()
            } catch (e: Exception) {
                fatalErrorHandler.executeHandler(e)
            }
        }
    }

    fun onNewNote() {
        viewModelScope.launch {
            try {
                val newNote = noteRepository.createNote()
                selectedNoteId = newNote.id.toString()
            } catch (e: Exception) {
                fatalErrorHandler.executeHandler(e)
            }
        }
    }

    fun onNewNoteFromImage(img: InputImage) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(img)
            .addOnSuccessListener { text ->
                if (img.bitmapInternal != null) {
                    viewModelScope.launch {
                        try {
                            noteRepository.createNoteFromFile(img.bitmapInternal!!, text.text)
                        } catch (e: Exception) {
                            fatalErrorHandler.executeHandler(e)
                        }
                    }
                }
            }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DADMAppApplication)
                val noteRepository = application.container.noteRepository
                val userRepository = application.container.userRepository
                val fatalErrorHandler = application.container.fatalErrorHandler
                HomePageViewModel(noteRepository, userRepository, fatalErrorHandler)
            }
        }
    }
}