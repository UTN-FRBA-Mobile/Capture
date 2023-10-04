package com.example.dadmapp.data

import com.example.dadmapp.model.note.Note
import com.example.dadmapp.network.NoteApiService

interface NoteRepository {
    suspend fun loadNotes(): List<Note>
}

class NetworkNoteRepository(
    private val noteApiService: NoteApiService
): NoteRepository {
    override suspend fun loadNotes(): List<Note> {
        return noteApiService.loadNotes()
    }
}