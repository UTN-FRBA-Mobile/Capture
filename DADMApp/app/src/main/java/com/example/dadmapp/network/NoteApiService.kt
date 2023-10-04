package com.example.dadmapp.network

import com.example.dadmapp.model.note.Note
import retrofit2.http.GET
import retrofit2.http.POST

interface NoteApiService {
    @GET("note")
    suspend fun loadNotes(): ArrayList<Note>

    @POST("note/create")
    suspend fun createNote(): Note
}