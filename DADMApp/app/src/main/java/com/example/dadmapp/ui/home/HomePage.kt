package com.example.dadmapp.ui.home

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dadmapp.R
import com.example.dadmapp.ui.components.NotePreview
import com.example.dadmapp.ui.theme.AccentRed1
import com.example.dadmapp.ui.theme.BgDark
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    onNoteClick: (noteId: String) -> Unit,
    homePageViewModel: HomePageViewModel = viewModel(factory = HomePageViewModel.Factory),
    onRecordAudio: () -> Unit
) {
    var showOptions by remember {
        mutableStateOf(false)
    }

    if (homePageViewModel.selectedNoteId != null) {
        LaunchedEffect(Unit) {
            onNoteClick(homePageViewModel.selectedNoteId!!)
        }
    }
    val ctx = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { urlList ->
            val uri = urlList[0]
            val img = InputImage.fromFilePath(ctx, uri)

            homePageViewModel.onNewNoteFromImage(img)
        }

    val btnSize = 45.dp

    val notesState = homePageViewModel.notes?.collectAsState()

    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(drawerState = drawerState,
        drawerContent = {
        ModalDrawerSheet {
            DrawerHeader()
            DrawerBody(items = listOf(
                DrawerItem(
                    id = "Primero",
                    title = "Primero",
                    icon = Icons.Filled.Home,
                    contentDesc = "Ir a primero"
                ),
                DrawerItem(
                    id = "Segundo",
                    title = "Ayuda",
                    icon = Icons.Filled.Info,
                    contentDesc = "Ayuda"
                )
            ), onItemClick = {
                when(it.id) {
                    "Primero" -> {/*TODO*/}
                    "Segundo" -> {/*TODO*/}
                }
            })
        }
    }) {


        Scaffold(
            containerColor = BgDark,
            topBar = {
                TopBar(viewModel = homePageViewModel, drawerState = drawerState)
            },
            floatingActionButton = {
                Column {
                    if (showOptions) {
                        DropdownMenu(
                            expanded = showOptions,
                            onDismissRequest = { showOptions = !showOptions },
                            modifier = Modifier.background(AccentRed1)
                        ) {
                            DropdownOption(
                                "Write",
                                { homePageViewModel.onNewNote() },
                                Icons.Filled.Create,
                                "Create note with text"
                            )
                            DropdownOption(
                                "From image",
                                { launcher.launch("image/*") },
                                painterResource(id = R.drawable.camera),
                                "Create note from image"
                            )
                            DropdownOption(
                                "From speech",
                                { onRecordAudio() },
                                painterResource(id = R.drawable.microphone),
                                "Create note from speech",
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
                                "Add note button",
                                tint = Color.White
                            )
                        }
                    }
                }
            }, content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentPadding = innerPadding
                ) {
                    items(notesState?.value?.size ?: 0) { idx ->
                        val note = notesState?.value?.get(idx)
                        if (note != null) {
                            Row(modifier = Modifier.padding(bottom = 20.dp)) {
                                NotePreview(
                                    title = note.title,
                                    content = note.content ?: "",
                                    date = note.createdAt,
                                    imageName = note.imageName,
                                    audioName = note.audioName,
                                    onNoteClick = { onNoteClick(note.id.toString()) }
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(viewModel: HomePageViewModel, drawerState: DrawerState) {
    var isSearchBarVisible by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier.background(BgDark)
    ) {
        Crossfade(
            targetState = isSearchBarVisible,
            animationSpec = tween(350), label = ""
        ) {
            if (it) {
                TopSearchBar(
                    onCloseIcon = {
                        isSearchBarVisible = false
                        viewModel.onSearchTextChanged("")
                    },
                    viewModel = viewModel
                )
            } else {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            TextButton(onClick = {}) {
                                Text("<Inserte logo aqui??>", color = Color.White)
                            }
                        }
                    },
                    navigationIcon = {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Burger menu button",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    actions = {
                        Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                            IconButton(
                                onClick = {
                                    isSearchBarVisible = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search button",
                                    tint = Color.LightGray
                                )
                            }
                        }
                        Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                            IconButton(
                                onClick = { /*TODO*/ }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "User button",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .requiredHeight(50.dp)
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
    onCloseIcon: () -> Unit,
    viewModel: HomePageViewModel
) {
    var searchQuery by remember { mutableStateOf("") }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .fillMaxWidth()
            .requiredHeight(50.dp),
        value = searchQuery,
        onValueChange = {
            searchQuery = it
            viewModel.onSearchTextChanged(searchQuery)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search icon",
                tint = Color.LightGray
            )
        },
        trailingIcon = {
            IconButton(onClick = { onCloseIcon() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close icon",
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
