package com.example.dadmapp.network

import com.example.dadmapp.model.note.Note
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NoteApiService {
    @GET("note")
    suspend fun loadNotes(): ArrayList<Note>

    @POST("note/create")
    suspend fun createNote(): Note

    @DELETE("note/{id}")
    suspend fun deleteNote(@Path("id") id: String)
}