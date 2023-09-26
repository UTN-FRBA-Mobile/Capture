package com.example.dadmapp.network

import com.example.dadmapp.model.note.Note
import retrofit2.http.GET

interface NoteApiService {
    @GET("note")
    suspend fun loadNotes(): List<Note>
}