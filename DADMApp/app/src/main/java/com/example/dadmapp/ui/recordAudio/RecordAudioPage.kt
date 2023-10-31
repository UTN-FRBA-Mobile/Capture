package com.example.dadmapp.ui.recordAudio

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.FileOutputStream
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("Recycle")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordAudioPage(
    recordPageViewModel: RecordPageViewModel = viewModel(factory = RecordPageViewModel.Factory),
    onCreatedNote: (noteId: String) -> Unit
) {
    val ctx = LocalContext.current
    var intent: Intent? = null
    val permissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    LaunchedEffect(permissionState) {
        val permissionResult = permissionState.status
        Log.d("INFO", "Permission status: $permissionResult")

        if (!permissionResult.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

    val l = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        recordPageViewModel.processRecognizerIntentResult(
            it,
            ctx.contentResolver,
            ctx.cacheDir,
            onCreatedNote
        )
    }

    Column {
        Button(onClick = {
            intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            intent?.let {
                it.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
                )

                it.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")

                it.putExtra("android.speech.extra.GET_AUDIO", true)
                it.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR")

                val p = PendingIntent.getActivity(ctx, 5000, it, PendingIntent.FLAG_IMMUTABLE)
                val s = IntentSenderRequest.Builder(p).build()

                l.launch(s)
            }
        }) {
            Text(text = "Start recording")
        }
    }
}