package com.example.dadmapp.ui.note

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotePage(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            Row {
                IconButton(onClick = { onBackClick() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back icon")
                }
            }
        }
    ) {
        Text(text = "asd")
    }
}