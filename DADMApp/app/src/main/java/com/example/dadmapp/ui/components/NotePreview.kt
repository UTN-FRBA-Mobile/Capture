package com.example.dadmapp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dadmapp.ui.theme.AccentRed1

fun getText(text: String): String {
    val maxLength = 200
    if (text.length <= maxLength) {
        return text
    }

    return text.take(maxLength - 3) + "..."
}

@Composable
fun NotePreview(
    title: String?,
    content: String,
    onNoteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .border(width = 2.dp, color = AccentRed1, shape = RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onNoteClick() }
    ) {
        if (title != null) {
            Row {
                CustomText(text = title, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
        }
        Row(modifier = Modifier
            .padding(vertical = 10.dp)) {
            CustomText(text = getText(content))
        }
        Row {
            CustomText(text = "16/09/2023", fontSize = 14.sp)
        }
    }
}
