package com.example.dadmapp.ui.home

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dadmapp.R
import com.example.dadmapp.model.tag.Tag
import com.example.dadmapp.ui.components.NotePreview
import com.example.dadmapp.ui.components.TagButton
import com.example.dadmapp.ui.theme.AccentRed1
import com.example.dadmapp.ui.theme.BgDark
import com.google.mlkit.vision.common.InputImage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    onNoteClick: (noteId: String) -> Unit,
    homePageViewModel: HomePageViewModel = viewModel(factory = HomePageViewModel.Factory),
    onRecordAudio: () -> Unit
) {
    var showOptions by remember { mutableStateOf(false) }

    var filterByTags: List<Tag> by remember { mutableStateOf(emptyList()) }

    if (homePageViewModel.selectedNoteId != null) {
        LaunchedEffect(Unit) {
            onNoteClick(homePageViewModel.selectedNoteId!!)
        }
    }
    val ctx = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        urlList ->
            val uri = urlList[0]
            val img = InputImage.fromFilePath(ctx, uri)

            homePageViewModel.onNewNoteFromImage(img)
    }

    val btnSize = 45.dp

    val notesState = homePageViewModel.notes?.collectAsState()
    val allTags = homePageViewModel.tags

    fun toggleTag(tag: Tag) {
        filterByTags = if (filterByTags.contains(tag)) {
            filterByTags.filterNot { it == tag }
        } else {
            filterByTags.plus(tag)
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shadowElevation = 10.dp,
                    color = AccentRed1,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .width(btnSize)
                        .height(btnSize)
                ) {
                    FilterMenu(
                        allTags = allTags,
                        filterByTags = filterByTags,
                        toggleTag = { tag -> toggleTag(tag) },
                    )
                }
            }
        },
        containerColor = BgDark,
        floatingActionButton = {
            Column {
                if (showOptions) {
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = { showOptions = !showOptions },
                        modifier = Modifier.background(AccentRed1)
                    ) {
                        DropdownOption(
                            stringResource(R.string.WRITE),
                            { homePageViewModel.onNewNote() },
                            Icons.Filled.Create,
                            stringResource(R.string.CREATE_NOTE_WITH_TEXT)
                        )
                        DropdownOption(
                            stringResource(R.string.FROM_IMAGE),
                            { launcher.launch("image/*") },
                            painterResource(id = R.drawable.camera),
                            stringResource(R.string.CREATE_NOTE_FROM_IMAGE)
                        )
                        DropdownOption(
                            stringResource(R.string.FROM_AUDIO),
                            { onRecordAudio() },
                            painterResource(id = R.drawable.microphone),
                            stringResource(R.string.CREATE_NOTE_FROM_SPEECH),
                        )
                    }
                }

                Surface(
                    shadowElevation = 10.dp,
                    color = AccentRed1,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .width(btnSize)
                        .height(btnSize)
                ) {
                    SmallFloatingActionButton(
                        onClick = { showOptions = !showOptions },
                        containerColor = AccentRed1,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            stringResource(R.string.ADD_NOTE_BUTTON),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 10.dp,
                    end = 10.dp,
                )
        ) {
            items(notesState?.value?.size ?: 0) { idx ->
                val note = notesState?.value?.get(idx)
                if (note != null && (filterByTags.isEmpty() || note.tags.any { t -> filterByTags.contains(t) })) {
                    Row(modifier = Modifier.padding(bottom = 20.dp)) {
                        NotePreview(
                            title = note.title,
                            content = note.content ?: "",
                            date = note.createdAt,
                            imageName = note.imageName,
                            audioName = note.audioName,
                            tags = note.tags,
                            onNoteClick = { onNoteClick(note.id.toString()) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterMenu(
    toggleTag: (tag: Tag) -> Unit,
    allTags: List<Tag>,
    filterByTags: List<Tag>,
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        // Icon
        IconButton(onClick = { expanded = true }) {
            Icon(
                painterResource(id = R.drawable.filter),
                contentDescription = stringResource(R.string.FILTER_BY_TAGS),
                tint = Color.Gray
            )
        }

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(AccentRed1)
        ) {
            allTags.forEach { t ->
                DropdownOption(
                    text = t.name,
                    onClick = { toggleTag(t) },
                    painterResource = if (filterByTags.contains(t)) {
                        painterResource(id = R.drawable.check)
                    } else { painterResource(id = R.drawable.square) },
                    contentDescription = t.name,
                )
            }
        }
    }
}
