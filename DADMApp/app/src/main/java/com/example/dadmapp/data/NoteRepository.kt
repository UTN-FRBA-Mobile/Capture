package com.example.dadmapp.data

import android.util.Log
import com.example.dadmapp.network.NoteApiService

interface NoteRepository {
    suspend fun loadNotes(): Unit
}

class NetworkNoteRepository(
    private val noteApiService: NoteApiService
): NoteRepository {
    override suspend fun loadNotes() {
        try {
            Log.d("INFO", "\n\nLOADING NOTES\n\n")
            val res = noteApiService.loadNotes()
            Log.d("INFO", res.toString())
        } catch (e: Exception) {
            e.message?.let { Log.d("ERR", it) }
        }
    }
}