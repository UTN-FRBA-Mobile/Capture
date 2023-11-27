package com.example.dadmapp.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dadmapp.R
import com.example.dadmapp.model.note.Note
import com.example.dadmapp.model.tag.Tag
import com.example.dadmapp.ui.components.NotePreview
import com.example.dadmapp.ui.home.components.FloatingButton
import com.example.dadmapp.ui.home.components.TopBar
import com.example.dadmapp.ui.theme.AccentRed1
import com.example.dadmapp.ui.theme.BgDark
import com.example.dadmapp.ui.theme.LightRed
import kotlinx.coroutines.launch

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
            !note.title.lowercase().contains(searchTerm.lowercase())
        )
    ) {
        return false
    }

    return true
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePage(
    onNoteClick: (noteId: String) -> Unit,
    homePageViewModel: HomePageViewModel = viewModel(factory = HomePageViewModel.Factory),
    onRecordAudio: () -> Unit,
    onLogOut: () -> Unit,
    onTagsColoursClick: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    val tagIconSize = 14.dp

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { 
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .background(BgDark)
                    .fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = null, tint = Color.White)
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text = homePageViewModel.username ?: "",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        )
                    }
                }
                Divider(
                    color = LightRed,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                        .clickable { onTagsColoursClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(id = R.drawable.tag),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.width(tagIconSize).height(tagIconSize)
                    )
                    Text(
                        text = "Customize tags",
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp),
                        textAlign = TextAlign.Left
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                        .clickable { homePageViewModel.onLogOut(onLogOut) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.ExitToApp,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.width(tagIconSize).height(tagIconSize)
                    )
                    Text(
                        text = "Log out",
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp),
                        textAlign = TextAlign.Left
                    )
                }
            }
        }
    ) {
        PageContent(
            onNoteClick,
            homePageViewModel,
            onRecordAudio,
            onDrawerStateChange = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) {
                            open()
                        } else {
                            close()
                        }
                    }
                }
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageContent(
    onNoteClick: (noteId: String) -> Unit,
    homePageViewModel: HomePageViewModel = viewModel(factory = HomePageViewModel.Factory),
    onRecordAudio: () -> Unit,
    onDrawerStateChange: () -> Unit,
) {
    if (homePageViewModel.selectedNoteId != null) {
        LaunchedEffect(Unit) {
            onNoteClick(homePageViewModel.selectedNoteId!!)
        }
    }

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
                    TopBar(
                        homePageViewModel = homePageViewModel,
                        onDrawerStateChange = onDrawerStateChange
                    )
                }
            )
        },
        containerColor = BgDark,
        floatingActionButton = {
            FloatingButton(
                homePageViewModel,
                onRecordAudio
            )
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