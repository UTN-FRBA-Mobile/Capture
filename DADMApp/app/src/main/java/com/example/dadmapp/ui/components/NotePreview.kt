package com.example.dadmapp.ui.components

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dadmapp.LOCALHOST_URL
import com.example.dadmapp.ui.theme.AccentRed1
import com.example.dadmapp.utils.formattedDateStr

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
    date: String,
    imageName: String?,
    onNoteClick: () -> Unit
) {
    val radius = 5.dp

    Column(
        modifier = Modifier
            .border(width = 2.dp, color = AccentRed1, shape = RoundedCornerShape(radius))
            .fillMaxWidth()
            .clickable { onNoteClick() }
    ) {
        if (imageName != null) {
            Row {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("$LOCALHOST_URL/$imageName")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Image of the note",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(topEnd = radius, topStart = radius))
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
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
                CustomText(text = formattedDateStr(date), fontSize = 14.sp)
            }
        }
    }
}
