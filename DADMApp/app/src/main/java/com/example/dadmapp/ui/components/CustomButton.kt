package com.example.dadmapp.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.dadmapp.ui.theme.DisabledButton
import com.example.dadmapp.ui.theme.EnabledButton

@Composable
fun CustomButton(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
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
        Text(text = label)
    }
}