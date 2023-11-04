package com.example.dadmapp.ui.home

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dadmapp.R
import com.example.dadmapp.ui.components.NotePreview
import com.example.dadmapp.ui.theme.BgDark
import com.example.dadmapp.ui.theme.LightRed
import com.google.mlkit.vision.common.InputImage

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

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        urlList ->
            val uri = urlList[0]
            val img = InputImage.fromFilePath(ctx, uri)

            homePageViewModel.onNewNoteFromImage(img)
    }

    Scaffold(
        containerColor = BgDark,
        floatingActionButton = {
            Column {
                if (showOptions) {
                    DropdownMenu(
                        expanded = showOptions,
                        onDismissRequest = { showOptions = !showOptions },
                        modifier = Modifier.background(LightRed)
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

                SmallFloatingActionButton(
                    onClick = { showOptions = !showOptions },
                    shape = RoundedCornerShape(10.dp),
                    containerColor = LightRed,
                ) {
                    Icon(
                        Icons.Filled.Add,
                        "Add note button",
                        tint = Color.White
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            homePageViewModel.notes?.collectAsState()?.value?.map { note ->
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