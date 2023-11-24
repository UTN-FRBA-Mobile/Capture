package com.example.dadmapp.ui.home

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dadmapp.R
import com.example.dadmapp.model.note.Note
import com.example.dadmapp.model.tag.Tag
import com.example.dadmapp.ui.components.NotePreview
import com.example.dadmapp.ui.theme.AccentRed1
import com.example.dadmapp.ui.theme.BgDark
import com.google.mlkit.vision.common.InputImage
import java.util.Locale

fun shouldDisplayNote(
    note: Note,
    filterByTags: List<Tag>,
    searchTerm: String?
): Boolean {
    if (
        filterByTags.isNotEmpty() &&
        note.tags.none { t -> filterByTags.contains(t) }
    ) {
        return false
    }

    if (
        searchTerm != null &&
        (
            note.title == null ||
            !note.title.lowercase().contains(searchTerm)
        )
    ) {
        return false
    }

    return true
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    onNoteClick: (noteId: String) -> Unit,
    homePageViewModel: HomePageViewModel = viewModel(factory = HomePageViewModel.Factory),
    onRecordAudio: () -> Unit
) {
    var showOptions by remember { mutableStateOf(false) }

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .requiredHeight(45.dp)
                    .clip(RoundedCornerShape(50.dp)),
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = AccentRed1,
                    titleContentColor = BgDark
                ),
                actions = {
                    TopBar(homePageViewModel = homePageViewModel)
                }
            )
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
                if (
                    note != null &&
                    shouldDisplayNote(
                        note,
                        homePageViewModel.filterByTags,
                        homePageViewModel.searchTerm
                    )
                ) {
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
        IconButton(onClick = { expanded = true }) {
            Icon(
                painterResource(id = R.drawable.filter),
                contentDescription = stringResource(R.string.FILTER_BY_TAGS),
                tint = Color.White
            )
        }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(homePageViewModel: HomePageViewModel) {
    val allTags = homePageViewModel.tags

    fun toggleTag(tag: Tag) {
        homePageViewModel.filterByTags = if (homePageViewModel.filterByTags.contains(tag)) {
            homePageViewModel.filterByTags.filterNot { it == tag }
        } else {
            homePageViewModel.filterByTags.plus(tag)
        }
    }

    var isSearchBarVisible by remember {
        mutableStateOf(false)
    }

    Crossfade(
        targetState = isSearchBarVisible,
        animationSpec = tween(350),
        label = ""
    ) {
        if (it) {
            TopSearchBar(
                onCloseIcon = {
                    isSearchBarVisible = false
                    homePageViewModel.searchTerm = null
                },
                homePageViewModel
            )
        } else {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(R.string.BURGER_MENU_BUTTON),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { isSearchBarVisible = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(R.string.SEARCH_BUTTON),
                            tint = Color.LightGray
                        )
                    }
                    FilterMenu(
                        allTags = allTags,
                        filterByTags = homePageViewModel.filterByTags,
                        toggleTag = { tag -> toggleTag(tag) },
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(45.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(BgDark),
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = AccentRed1,
                    titleContentColor = BgDark
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
    onCloseIcon: () -> Unit,
    homePageViewModel: HomePageViewModel
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .fillMaxWidth()
            .requiredHeight(50.dp),
        value = homePageViewModel.searchTerm ?: "",
        onValueChange = {
            homePageViewModel.searchTerm = it
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.SEARCH_ICON),
                tint = Color.LightGray
            )
        },
        trailingIcon = {
            IconButton(onClick = { onCloseIcon() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.CLOSE_ICON),
                    tint = Color.White
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = AccentRed1,
            textColor = Color.White
        )
    )
}