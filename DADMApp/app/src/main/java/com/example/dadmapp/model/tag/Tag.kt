package com.example.dadmapp.model.tag

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val id: Int? = null,
    val name: String,
    val colour: String? = null
)