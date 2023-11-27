package com.example.dadmapp.ui.tags

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dadmapp.DADMAppApplication
import com.example.dadmapp.data.FatalErrorHandler
import com.example.dadmapp.data.NoteRepository
import com.example.dadmapp.model.tag.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TagsColoursViewModel(
    private val noteRepository: NoteRepository,
    private val fatalErrorHandler: FatalErrorHandler
): ViewModel() {
    var tags: MutableStateFlow<List<Tag>>? = MutableStateFlow(emptyList())

    init {
        tags = noteRepository.getTags()
    }

    fun onSaveEdits(edits: Map<String, String>, callback: () -> Unit) {
        viewModelScope.launch {
            try {
                if (edits.isNotEmpty()) {
                    noteRepository.updateTagsColours(edits)
                }
                callback()
            } catch (e: Exception) {
                fatalErrorHandler.executeHandler(e)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DADMAppApplication)
                val noteRepository = application.container.noteRepository
                val fatalErrorHandler = application.container.fatalErrorHandler
                TagsColoursViewModel(noteRepository, fatalErrorHandler)
            }
        }
    }
}