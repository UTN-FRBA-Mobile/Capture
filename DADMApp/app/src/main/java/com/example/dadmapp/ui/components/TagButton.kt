package com.example.dadmapp.ui.components

import android.util.Log
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
import androidx.compose.ui.res.stringResource
import com.example.dadmapp.R
import com.example.dadmapp.utils.hexColorToObj

@Composable
fun TagButton(
    modifier: Modifier = Modifier,
    tag: Tag,
    showDeletable: Boolean,
    enabled: Boolean,
    onClick: () -> Unit = {},
) {
    val colour = if (tag.colour != null) hexColorToObj(tag.colour) else AccentRed1

    val textColour = getTint(colour);

    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(50),
        modifier = modifier
            .height(24.dp)
            .padding(end = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colour,
            disabledContainerColor = colour,
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
            Text(text = "#${tag.name}", fontSize = 12.sp, color = textColour)
            if (showDeletable) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.DELETE_TAG),
                    tint = textColour,
                )
            }
        }
    }
}

fun getTint(bgColor: Color): Color {
    val red = bgColor.red * 255
    val green = bgColor.green * 255
    val blue = bgColor.blue * 255

    if ((red * 0.299 + green * 0.587 + blue * 0.114) > 150) {
        return Color.Black
    }

    return Color.White
}