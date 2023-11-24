package com.example.dadmapp.ui.home.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dadmapp.R
import com.example.dadmapp.ui.home.DropdownOption
import com.example.dadmapp.ui.home.HomePageViewModel
import com.example.dadmapp.ui.theme.AccentRed1
import com.google.mlkit.vision.common.InputImage

@Composable
fun FloatingButton(
    homePageViewModel: HomePageViewModel,
    onRecordAudio: () -> Unit
) {
    val ctx = LocalContext.current

    var showOptions by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            urlList ->
        val uri = urlList[0]
        val img = InputImage.fromFilePath(ctx, uri)

        homePageViewModel.onNewNoteFromImage(img)
    }

    val btnSize = 45.dp

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
            modifier = androidx.compose.ui.Modifier
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