package com.example.dadmapp.network.body

import kotlinx.serialization.Serializable

@Serializable
data class UpdateNoteBody(
    val title: String,
    val content: String
)