package com.example.dadmapp.model.singup

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest (
    val username: String,
    val password: String
)