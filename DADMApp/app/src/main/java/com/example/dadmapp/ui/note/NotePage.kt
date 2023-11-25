package com.example.dadmapp.ui.note

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.example.dadmapp.R
import com.example.dadmapp.model.tag.Tag
import com.example.dadmapp.ui.components.AudioPlayer
import com.example.dadmapp.ui.components.NetworkErrorDialog
import com.example.dadmapp.ui.components.TagButton
import com.example.dadmapp.ui.theme.BgDark
import com.example.dadmapp.ui.theme.LightRed
import com.example.dadmapp.utils.formattedDateStr
import com.example.dadmapp.utils.formattedTimeStr
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.launch
import java.io.IOException

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

    var tags by remember { mutableStateOf(note.tags) }
    var newTag by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var showNetworkErrorDialog by remember { mutableStateOf(false) }

    var titleVal by remember {
        mutableStateOf(note.title)
    }

    var contentVal by remember {
        mutableStateOf(note.content)
    }

    var editMode by remember {
        mutableStateOf(false)
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    fun onDeleteNote() {

        coroutineScope.launch {
            try {
                isLoading = true
                notePageViewModel.deleteNote(noteId)
                onBackClick()
            } catch (e: IOException) {
                showNetworkErrorDialog = true
            } finally {
                isLoading = false
            }

        }
    }

    fun onBack() {
        coroutineScope.launch {

            try {
                isLoading = true
                if (titleVal.isNullOrEmpty() && contentVal.isNullOrEmpty()) {
                    notePageViewModel.deleteNote(noteId)
                } else {
                    notePageViewModel.updateNote(noteId, titleVal, contentVal, tags)
                }
                onBackClick()

            } catch (e: IOException) {
                showNetworkErrorDialog = true
            } finally {
                isLoading = false
            }

        }
    }

    Scaffold(
        containerColor = BgDark,

        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {

                    onBack()

                }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.BACK_ICON),
                        tint = Color.White
                    )
                }

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                ) {

                    if (tags.size < 3 && editMode) {
                        Button(
                            onClick = { showDialog = true },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .height(24.dp)
                                .align(Alignment.CenterVertically)
                                .padding(end = 4.dp),
                            contentPadding = PaddingValues(
                                horizontal = 8.dp,
                                vertical = 0.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LightRed
                            )
                        ) {
                            Text(stringResource(R.string.ADD_TAG), fontSize = 12.sp)
                        }
                    }

                    tags.forEach { tag ->
                        TagButton(
                            tag = tag,
                            onClick = { tags -= tag },
                            showDeletable = editMode,
                            enabled = editMode
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    IconButton(onClick = { editMode = !editMode }) {
                        Icon(
                            if (editMode) painterResource(id = R.drawable.baseline_edit_24) else painterResource(
                                id = R.drawable.baseline_edit_off_24
                            ),
                            contentDescription = stringResource(R.string.EDIT_NOTE),
                            tint = if (editMode) Color.White else Color.Gray
                        )
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.DELETE_NOTE),
                            tint = Color.White
                        )
                    }
                }
            }
        },


        bottomBar = {
            Text(
                text = "${stringResource(R.string.LAST_EDITED)} " +
                        formattedDateStr(note.updatedAt) +
                        " ${stringResource(R.string.LAST_EDITED_AT_SUBSTR)} " +
                        formattedTimeStr(note.updatedAt),
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp
            )
        }
    ) { paddingValues ->
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
                        contentDescription = stringResource(R.string.IMAGE_OF_THE_NOTE),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(5.dp))
                    )
                }
            }

            if (note.audioName != null) {
                Row {
                    AudioPlayer(note.audioName)
                }
            }

            Row {
                TitleTextField(
                    value = titleVal ?: "",
                    onTitleChange = { titleVal = it },
                    readOnly = !editMode
                )
            }

            if (editMode) {
                Row {
                    ContentTextField(
                        value = contentVal ?: "",
                        onContentChange = { contentVal = it })
                }
            } else {
                Row {
                    MarkdownText(
                        markdown = contentVal ?: "",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        fontSize = 16.sp
                    )
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(stringResource(R.string.ADD_TAG)) },
                    text = {
                        TextField(
                            value = newTag,
                            onValueChange = { newTag = it },
                            singleLine = true
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                if (newTag.isNotBlank()) {
                                    tags = tags + Tag(name = newTag, createdAt = "", updatedAt = "")
                                    newTag = ""
                                    showDialog = false
                                }
                            }
                        ) { Text("Add") }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDialog = false
                        }) { Text(stringResource(R.string.CANCEL)) }
                    }
                )
            }

            DeleteAlertDialog(
                show = showDeleteDialog,
                onDismiss = { showDeleteDialog = false },
                onConfirm = { onDeleteNote() }
            )

            NetworkErrorDialog(
                show = showNetworkErrorDialog,
                onDismiss = { showNetworkErrorDialog = false }
            )

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTextField(
    value: String,
    onTitleChange: (newTitle: String) -> Unit,
    readOnly: Boolean = false
) {
    val fontSize = 24.sp

    TextField(
        value = value,
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.TITLE),
                fontWeight = FontWeight.Bold,
                fontSize = fontSize
            )
        },
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
        textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = fontSize),
        readOnly = readOnly
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentTextField(value: String, onContentChange: (newContent: String) -> Unit) {
    val fontSize = 16.sp

    TextField(
        value = value,
        onValueChange = { onContentChange(it) },
        placeholder = {
            Text(
                text = stringResource(R.string.WRITE_SOMETHING),
                fontSize = fontSize
            )
        },
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

@Composable
fun DeleteAlertDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = stringResource(R.string.CONFIRM))
                }

            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = stringResource(R.string.CANCEL))
                }
            },
            title = { Text(text = stringResource(R.string.DELETE_NOTE_CONFIRMATION_TEXT)) }
        )
    }
}
