package com.example.dadmapp.network

import com.example.dadmapp.model.note.Note
import com.example.dadmapp.network.body.UpdateNoteBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteApiService {
    @GET("note")
    suspend fun loadNotes(): ArrayList<Note>

    @POST("note/create")
    suspend fun createNote(): Note

    @DELETE("note/{id}")
    suspend fun deleteNote(@Path("id") id: String)

    @PUT("note/{id}")
    suspend fun updateNote(@Path("id") id: String, @Body body: UpdateNoteBody)
}