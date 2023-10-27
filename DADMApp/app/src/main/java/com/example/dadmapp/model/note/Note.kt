package com.example.dadmapp.model.note

import kotlinx.serialization.Serializable

@Serializable
data class Note (
    val id: Int,
    val username: String,
    val title: String? = null,
    val content: String? = null,
    val imageName: String? = null,
    val audioName: String? = null,
    val createdAt: String,
    val updatedAt: String
)