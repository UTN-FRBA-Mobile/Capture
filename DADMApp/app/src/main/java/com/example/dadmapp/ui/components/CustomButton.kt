package com.example.dadmapp.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.dadmapp.ui.theme.AccentRed1

@Composable
fun CustomButton(
    label: String,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentRed1
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = label)
    }
}