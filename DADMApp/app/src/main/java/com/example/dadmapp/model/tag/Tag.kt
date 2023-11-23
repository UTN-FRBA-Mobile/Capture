package com.example.dadmapp.model.tag

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val name: String,
    val colour: String? = null,
    val createdAt: String,
    val updatedAt: String
)