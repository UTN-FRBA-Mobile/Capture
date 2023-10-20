package com.example.dadmapp.ui.recordAudio

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.dadmapp.utils.AudioRecorder
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordAudioPage(
    recordPageViewModel: RecordPageViewModel = viewModel(factory = RecordPageViewModel.Factory)
) {
    val ctx = LocalContext.current
    val recorder = AudioRecorder(ctx)
    val permissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    var file: File? = null

    LaunchedEffect(permissionState) {
        val permissionResult = permissionState.status

        if (!permissionResult.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

    Column {
        Button(onClick = {
            File(ctx.cacheDir, "audio.mp3").also {
                recorder.start(it)
                file = it
            }
        }) {
            Text(text = "Start recording")
        }
        Button(onClick = {
            recorder.stop()
            file?.let { recordPageViewModel.onNewNoteFromAudio(it) }
            file = null
        }) {
            Text(text = "Stop recording")
        }
    }
}