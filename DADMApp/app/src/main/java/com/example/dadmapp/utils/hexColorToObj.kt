package com.example.dadmapp.utils

import androidx.compose.ui.graphics.Color

fun hexColorToObj(text: String): Color {
    return Color(android.graphics.Color.parseColor(text))
}