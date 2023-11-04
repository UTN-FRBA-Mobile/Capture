package com.example.dadmapp.ui.home

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val dimensions = 20.dp

@Composable
fun DropdownOption(
    text: String,
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String
) {
    TextButton(onClick = { onClick() }) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.width(dimensions).height(dimensions)
        )
        Text(text, color = Color.White, modifier = Modifier.padding(start = 7.5.dp, end = 5.dp))
    }
}

@Composable
fun DropdownOption(
    text: String,
    onClick: () -> Unit,
    painterResource: Painter,
    contentDescription: String
) {
    TextButton(onClick = { onClick() }) {
        Icon(
            painterResource,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.width(dimensions).height(dimensions)
        )
        Text(text, color = Color.White, modifier = Modifier.padding(start = 7.5.dp, end = 5.dp))
    }
}