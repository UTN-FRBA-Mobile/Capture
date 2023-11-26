package com.example.dadmapp.network.body

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTagsColoursBody (
    val data: Map<String, String>
    )