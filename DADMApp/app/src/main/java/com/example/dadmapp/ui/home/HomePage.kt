package com.example.dadmapp.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomePage() {
    val viewModel: HomePageViewModel = viewModel(factory = HomePageViewModel.Factory)

    Column(
        modifier = Modifier.fillMaxSize()
        ) {
        Row {
            Text("HELLO!", color = Color.White)
        }
    }
}