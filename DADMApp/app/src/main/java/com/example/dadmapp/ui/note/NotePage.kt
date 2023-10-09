package com.example.dadmapp.ui.note

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dadmapp.ui.theme.BgDark
import com.example.dadmapp.ui.theme.LightRed
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotePage(
    onBackClick: () -> Unit,
    notePageViewModel: NotePageViewModel = viewModel(factory = NotePageViewModel.Factory),
    noteId: String
) {
    val coroutineScope = rememberCoroutineScope()

    fun onDeleteNote(id: String) {
        coroutineScope.launch {
            notePageViewModel.deleteNote(id)
            onBackClick()
        }
    }

    Scaffold(
        containerColor = BgDark,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { onBackClick() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back icon", tint = Color.White)
                }
                IconButton(onClick = { onDeleteNote(noteId) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete note", tint = Color.White)
                }
            }
        },
        bottomBar = {
            Text(
                text = "Ultima vez editado...",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp
            )
        }
    ) {
        paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            Row {
                TitleTextField()
            }
            Row {
                ContentTextField()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTextField() {
    var value by remember {
        mutableStateOf("")
    }

    val fontSize = 24.sp

    TextField(
        value = value,
        singleLine = true,
        placeholder = { Text(text = "Title", fontWeight = FontWeight.Bold, fontSize = fontSize) },
        onValueChange = { value = it },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            containerColor = BgDark,
            focusedLabelColor = LightRed,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = Modifier.border(0.dp, Color.Transparent),
        textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = fontSize)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentTextField() {
    var value by remember {
        mutableStateOf("")
    }

    val fontSize = 16.sp

    TextField(
        value = value,
        onValueChange = { value = it },
        placeholder = { Text(text = "Write something here...", fontSize = fontSize) },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            containerColor = BgDark,
            focusedLabelColor = LightRed,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .border(0.dp, Color.Transparent)
            .fillMaxWidth()
    )
}