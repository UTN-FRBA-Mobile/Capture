package com.example.dadmapp.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun CustomText(
    text: String,
    fontWeight: FontWeight = FontWeight.Light,
    textAlign: TextAlign = TextAlign.Left,
    lineHeight: TextUnit = 20.sp,
    fontSize: TextUnit = 16.sp,
    color: Color = Color.White
) {
    Text(
        text = text,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        lineHeight = lineHeight,
        fontSize = fontSize
    )
}