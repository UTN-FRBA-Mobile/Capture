package com.example.dadmapp.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dadmapp.ui.components.NotePreview

@Composable
fun HomePage() {
    val homePageViewModel: HomePageViewModel = viewModel(factory = HomePageViewModel.Factory)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
        ) {
        homePageViewModel.notes.map { note ->
            Row(modifier = Modifier.padding(bottom = 15.dp)) {
                NotePreview(title = note.title, content = note.content ?: "")
            }
        }
    }
}