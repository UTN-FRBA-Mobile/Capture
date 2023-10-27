package com.example.dadmapp.model.singup

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse (
    val status: Int,
    val message: String
)
