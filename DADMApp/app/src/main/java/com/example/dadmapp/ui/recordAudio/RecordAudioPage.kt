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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dadmapp.R
import com.example.dadmapp.ui.theme.LightRed
import java.io.FileOutputStream
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("Recycle")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordAudioPage(
    recordPageViewModel: RecordPageViewModel = viewModel(factory = RecordPageViewModel.Factory),
    onCreatedNote: (noteId: String) -> Unit,
    onBack: () -> Unit
) {
    val ctx = LocalContext.current
    var intent: Intent? = null
    val permissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    LaunchedEffect(permissionState) {
        val permissionResult = permissionState.status
        Log.d("INFO", "Permission status: $permissionResult")

        if (!permissionState.status.isGranted) {
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

    fun launchIntent() {
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
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
    ) {
        Row {
            IconButton(onClick = { onBack() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back icon",
                    tint = Color.White
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (permissionState.status.isGranted) {
                Button(
                    onClick = { launchIntent() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightRed
                    )
                ) {
                    Text(text = stringResource(R.string.START_RECORDING))
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 30.dp,
                            top = 40.dp,
                            end = 30.dp
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier
                            .border(2.dp, Color.Red, CircleShape)
                            .padding(5.dp)
                            .size(80.dp),
                        tint = Color.Red
                    )
                    Text(
                        text = stringResource(R.string.ALLOW_ACCESS_TO_MICROPHONE),
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 25.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                    Button(
                        onClick = { permissionState.launchPermissionRequest() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightRed
                        )
                    ) {
                        Text(text = stringResource(R.string.REQUEST_PERMISSION))
                    }
                }
            }
        }
    }
}