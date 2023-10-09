package com.example.dadmapp.data

import android.util.Log
import com.example.dadmapp.model.note.Note
import com.example.dadmapp.network.NoteApiService
import com.example.dadmapp.network.body.UpdateNoteBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.lang.Exception
import java.time.Instant

interface NoteRepository {
    suspend fun loadNotes(): MutableStateFlow<List<Note>>
    fun getNoteById(id: String): Note
    suspend fun createNote(): Note
    suspend fun deleteNote(id: String)
    suspend fun updateNote(id: String, title: String?, content: String?)
}

class NetworkNoteRepository(
    private val noteApiService: NoteApiService
): NoteRepository {
    private var notesLoaded = false
    private var notesFlow = MutableStateFlow(emptyList<Note>())

    override suspend fun loadNotes(): MutableStateFlow<List<Note>> {
        if (notesLoaded) {
            Log.d("INFO", "Notes already loaded")
            return notesFlow
        }

        Log.d("INFO", "Notes not loaded. Loading again")
        val newNotes = noteApiService.loadNotes()
        notesFlow.update { newNotes }
        notesLoaded = true
        return notesFlow
    }

    override fun getNoteById(id: String): Note {
        Log.d("INFO", "Retrieving note $id")
        val r =  notesFlow.value.find { n -> n.id.toString() == id }

        if (r === null) {
            throw Exception("Tried to retrieve note that does not exist")
        }

        return r
    }

    override suspend fun createNote(): Note {
        val note = noteApiService.createNote()
        notesFlow.update { notes -> notes + note }
        return note
    }

    override suspend fun deleteNote(id: String) {
        noteApiService.deleteNote(id);
        notesFlow.update { notes -> notes.filter { n -> n.id.toString() != id } }
    }

    override suspend fun updateNote(id: String, title: String?, content: String?) {
        val bodyTitle = title ?: ""
        val bodyContent = content ?: ""
        val body = UpdateNoteBody(bodyTitle, bodyContent)
        noteApiService.updateNote(id, body)
        notesFlow.update { arr -> arr.map {
                n ->
                if (n.id.toString() == id) {
                    n.copy(
                        title = bodyTitle,
                        content = bodyContent,
                        updatedAt = Instant.now().toString()
                    )
                } else {
                    n
                }
        } }
    }
}