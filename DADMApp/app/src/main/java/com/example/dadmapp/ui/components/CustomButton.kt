package com.example.dadmapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dadmapp.ui.theme.DisabledButton
import com.example.dadmapp.ui.theme.EnabledButton

@Composable
fun CustomButton(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    showLoading: Boolean

) {

    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = EnabledButton,
            disabledContainerColor = DisabledButton
        ),
        shape = RoundedCornerShape(10.dp),
        enabled = enabled
    ) {
        if (showLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(28.dp).fillMaxWidth()
            )
        } else {
            Text(text = label)
        }
    }
}