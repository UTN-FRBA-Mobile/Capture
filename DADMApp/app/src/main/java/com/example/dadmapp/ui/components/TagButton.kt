package com.example.dadmapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dadmapp.model.tag.Tag
import com.example.dadmapp.ui.theme.AccentRed1
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text

@Composable
fun TagButton(
    modifier: Modifier = Modifier,
    tag: Tag,
    showDeletable: Boolean,
    enabled: Boolean,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(50),
        modifier = modifier
            .height(24.dp)
            .padding(end = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentRed1,
            disabledContainerColor = AccentRed1,
            contentColor = Color.White,
            disabledContentColor = Color.Gray
        ),
        contentPadding = PaddingValues(
            horizontal = 8.dp,
            vertical = 0.dp
        ),
        enabled = enabled
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "#${tag.name.uppercase()}", fontSize = 12.sp)
            if (showDeletable) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Delete tag",
                    tint = Color.White,
                )
            }
        }
    }
}
