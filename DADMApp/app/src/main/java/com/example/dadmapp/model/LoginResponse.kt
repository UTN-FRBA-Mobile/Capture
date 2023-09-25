package com.example.dadmapp.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)