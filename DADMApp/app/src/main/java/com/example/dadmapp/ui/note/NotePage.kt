package com.example.dadmapp.ui.note

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dadmapp.IMGS_PATH
import com.example.dadmapp.LOCALHOST_URL
import com.example.dadmapp.ui.theme.BgDark
import com.example.dadmapp.ui.theme.LightRed
import com.example.dadmapp.utils.formattedDateStr
import com.example.dadmapp.utils.formattedTimeStr
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
    val note = notePageViewModel.getNote(noteId)

    var titleVal by remember {
        mutableStateOf(note.title)
    }

    var contentVal by remember {
        mutableStateOf(note.content)
    }

    fun onDeleteNote() {
        coroutineScope.launch {
            notePageViewModel.deleteNote(noteId)
            onBackClick()
        }
    }

    fun onBack() {
        coroutineScope.launch {
            notePageViewModel.updateNote(noteId, titleVal, contentVal)
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
                IconButton(onClick = { onBack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back icon", tint = Color.White)
                }
                IconButton(onClick = { onDeleteNote() }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete note", tint = Color.White)
                }
            }
        },
        bottomBar = {
            Text(
                text = "Editado por ultima vez el " + formattedDateStr(note.updatedAt) + " a las " + formattedTimeStr(note.updatedAt),
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
                    bottom = paddingValues.calculateBottomPadding(),
                )
        ) {
            if (note.imageName != null) {
                Row(
                    modifier = Modifier.padding(horizontal = 15.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("$LOCALHOST_URL/$IMGS_PATH/${note.imageName}")
                            .crossfade(true)
                            .build(),
                        contentDescription = "Image of the note",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(5.dp))
                    )
                }
            }
            Row {
                TitleTextField(value = titleVal ?: "", onTitleChange = { titleVal = it })
            }
            Row {
                ContentTextField(value = contentVal ?: "", onContentChange = { contentVal = it })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTextField(value: String, onTitleChange: (newTitle: String) -> Unit) {
    val fontSize = 24.sp

    TextField(
        value = value,
        singleLine = true,
        placeholder = { Text(text = "Title", fontWeight = FontWeight.Bold, fontSize = fontSize) },
        onValueChange = { onTitleChange(it) },
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
fun ContentTextField(value: String, onContentChange: (newContent: String) -> Unit) {
    val fontSize = 16.sp

    TextField(
        value = value,
        onValueChange = { onContentChange(it) },
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