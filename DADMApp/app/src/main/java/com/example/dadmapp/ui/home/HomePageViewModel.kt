package com.example.dadmapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dadmapp.data.NoteRepository
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import com.example.dadmapp.DADMAppApplication
import kotlinx.coroutines.launch

class HomePageViewModel(private val noteRepository: NoteRepository): ViewModel() {
    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            noteRepository.loadNotes()
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