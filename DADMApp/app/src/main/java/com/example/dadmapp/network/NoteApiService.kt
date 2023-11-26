package com.example.dadmapp.network

import com.example.dadmapp.model.note.Note
import com.example.dadmapp.network.body.UpdateNoteBody
import com.example.dadmapp.network.body.UpdateTagsColoursBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface NoteApiService {
    @GET("note")
    suspend fun loadNotes(): ArrayList<Note>

    @POST("note/create")
    suspend fun createNote(): Note

    @Multipart
    @POST("note/create/image")
    suspend fun createNoteFromImage(
        @Part("text") noteText: RequestBody,
        @Part image: MultipartBody.Part
    ): Note

    @Multipart
    @POST("note/create/audio")
    suspend fun createNoteFromAudio(
        @Part("text") noteText: RequestBody,
        @Part audio: MultipartBody.Part
    ): Note

    @DELETE("note/{id}")
    suspend fun deleteNote(@Path("id") id: String)

    @PUT("note/{id}")
    suspend fun updateNote(@Path("id") id: String, @Body body: UpdateNoteBody): Note

    @POST("note/tags/colors")
    suspend fun updateColours(@Body body: UpdateTagsColoursBody)
}