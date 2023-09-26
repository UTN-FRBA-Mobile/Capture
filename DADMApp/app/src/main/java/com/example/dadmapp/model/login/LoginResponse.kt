package com.example.dadmapp.model.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)