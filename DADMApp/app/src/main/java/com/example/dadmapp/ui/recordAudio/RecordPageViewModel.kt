package com.example.dadmapp.ui.recordAudio

import android.content.ContentResolver
import android.speech.RecognizerIntent
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dadmapp.DADMAppApplication
import com.example.dadmapp.data.NoteRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class RecordPageViewModel(private val noteRepository: NoteRepository): ViewModel() {
    fun processRecognizerIntentResult(
        result: ActivityResult,
        contentResolver: ContentResolver,
        cacheDir: File,
        onCreatedNote: (noteId: String) -> Unit
    ) {
        viewModelScope.launch {
            val uri = result.data?.data
            val text = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.first()

            uri?.let {
                text?.let {
                    val inputStream = contentResolver.openInputStream(uri) ?: return@let

                    val f = File(cacheDir, "audio.mp3")

                    val outputStream = FileOutputStream(f)

                    var read = -1
                    do {
                        read = inputStream.read()

                        if (read != -1) {
                            outputStream.write(read)
                        }
                    } while (read != -1)

                    inputStream.close()
                    outputStream.close()

                    val note = noteRepository.createNoteFromAudio(f, text)
                    f.delete()
                    onCreatedNote(note.id.toString())
                }
            }
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