package com.example.dadmapp.data

import android.graphics.Bitmap
import android.util.Log
import com.example.dadmapp.model.note.Note
import com.example.dadmapp.network.NoteApiService
import com.example.dadmapp.network.body.UpdateNoteBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.Instant

interface NoteRepository {
    suspend fun loadNotes(): MutableStateFlow<List<Note>>
    fun getNoteById(id: String): Note
    suspend fun createNote(): Note
    suspend fun createNoteFromFile(image: Bitmap, imgText: String): Note
    suspend fun createNoteFromAudio(audio: File, text: String): Note
    suspend fun deleteNote(id: String)
    suspend fun updateNote(id: String, title: String?, content: String?)
}

class NetworkNoteRepository(
    private val noteApiService: NoteApiService
): NoteRepository {
    private var notesLoaded = false
    private var notes = MutableStateFlow(emptyList<Note>())

    override suspend fun loadNotes(): MutableStateFlow<List<Note>> {
        if (notesLoaded) {
            Log.d("INFO", "Notes already loaded")
            return notes
        }

        Log.d("INFO", "Notes not loaded. Loading again")

        val newNotes = noteApiService.loadNotes()
        notes.update { newNotes }
        notesLoaded = true
        return notes
    }

    override fun getNoteById(id: String): Note {
        val r =  notes.value.find { n -> n.id.toString() == id }

        if (r === null) {
            throw Exception("Tried to retrieve note that does not exist")
        }

        return r
    }

    override suspend fun createNote(): Note {
        val note = noteApiService.createNote()
        notes.update { notes -> notes + note }
        return note
    }

    override suspend fun createNoteFromFile(image: Bitmap, imgText: String): Note {
        val outputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val dataAsByteArray = outputStream.toByteArray()

        val bodyData = dataAsByteArray.toRequestBody(
            "image/*".toMediaTypeOrNull(),
            0,
            dataAsByteArray.size
        )

        val bodyPart = MultipartBody.Part.createFormData(
            "file",
            "image.jpeg",
            bodyData
        )

        val textData = imgText.toRequestBody(MultipartBody.FORM)

        val note = noteApiService.createNoteFromImage(textData, bodyPart)
        notes.update { notes -> notes + note }
        return note
    }

    override suspend fun createNoteFromAudio(audio: File, text: String): Note {
        val bytes = audio.readBytes()

        val bodyData = bytes.toRequestBody(
            "audio/*".toMediaTypeOrNull(),
            0,
            bytes.size
        )

        val bodyPart = MultipartBody.Part.createFormData(
            "file",
            "audio.mp3",
            bodyData
        )

        val audioData = text.toRequestBody(MultipartBody.FORM)

        val note = noteApiService.createNoteFromAudio(audioData, bodyPart)
        notes.update { notes -> notes + note }
        return note
    }

    override suspend fun deleteNote(id: String) {
        noteApiService.deleteNote(id);
        notes.update { notes -> notes.filter { n -> n.id.toString() != id } }
    }

    override suspend fun updateNote(id: String, title: String?, content: String?) {
        val bodyTitle = title ?: ""
        val bodyContent = content ?: ""
        val body = UpdateNoteBody(bodyTitle, bodyContent)
        noteApiService.updateNote(id, body)
        notes.update { arr -> arr.map {
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