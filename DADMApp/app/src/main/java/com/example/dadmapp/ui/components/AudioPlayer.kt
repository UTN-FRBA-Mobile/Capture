package com.example.dadmapp.ui.components

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dadmapp.AUDIO_PATH
import com.example.dadmapp.LOCALHOST_URL
import com.example.dadmapp.R
import com.example.dadmapp.ui.theme.AccentRed1
import com.example.dadmapp.ui.theme.LightRed
import com.example.dadmapp.utils.msToMinutesAndSeconds
import kotlinx.coroutines.delay

@Composable
fun AudioPlayer(
    audioName: String
) {
    var loaded by remember {
        mutableStateOf(false)
    }

    var playing by remember {
        mutableStateOf(false)
    }

    var duration by remember {
        mutableIntStateOf(-1)
    }

    var currentValue by remember {
        mutableIntStateOf(0)
    }

    val ctx = LocalContext.current
    val url = remember { "$LOCALHOST_URL/$AUDIO_PATH/$audioName" }

    val mediaPlayer = remember { MediaPlayer() }

    val progressFactor: Long = remember { 100 }

    // Necesitamos esto?
    if (!loaded) {
        mediaPlayer.setAudioAttributes(
            AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        mediaPlayer.setDataSource(ctx, Uri.parse(url))
        mediaPlayer.setOnPreparedListener {
                mp ->
            loaded = true
            duration = mp.duration
        }
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnCompletionListener {
            playing = false
            currentValue = duration
        }

        return
    }

    if (playing) {
        LaunchedEffect(Unit) {
            if (currentValue == duration) {
                currentValue = 0
            }

            while (playing) {
                delay(progressFactor)

                val newVal = currentValue + progressFactor

                if (newVal > duration) {
                    currentValue = duration
                } else {
                    currentValue += progressFactor.toInt()
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 10.dp,
                top = 10.dp,
                end = 10.dp
            )
            .background(LightRed, shape = RoundedCornerShape(20.dp))
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayButton(
            playing,
            {
                playing = true
                mediaPlayer.start()
            },
            {
                mediaPlayer.pause()
                playing = false
            }
        )
        LinearProgressIndicator(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 7.5.dp),
            progress = (currentValue.toFloat() / duration.toFloat()),
            trackColor = Color.White,
            color = AccentRed1
        )
        Text(
            msToMinutesAndSeconds(if (playing) currentValue else duration),
            Modifier.padding(end = 7.5.dp),
            color = Color.White
        )
    }
}

@Composable
fun PlayButton(
    playing: Boolean,
    onPlayStart: () -> Unit,
    onPlayStop: () -> Unit
) {
    if (playing) {
        IconButton(
            onClick = { onPlayStop() },
            contentDescription = stringResource(R.string.STOP_AUDIO),
            painterResource = painterResource(id = R.drawable.pause_icon)
        )
    } else {
        IconButton(
            onClick = { onPlayStart() },
            icon = Icons.Filled.PlayArrow,
            contentDescription = stringResource(R.string.PLAY_AUDIO)
        )
    }
}

@Composable
fun IconButton(
    onClick: () -> Unit,
    contentDescription: String,
    icon: ImageVector,
) {
    val size = 25.dp

    Button(
        onClick = { onClick() },
        modifier = Modifier
            .width(size)
            .height(size),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentRed1
        ),
        contentPadding = PaddingValues(5.dp)
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun IconButton(
    onClick: () -> Unit,
    contentDescription: String,
    painterResource: Painter
) {
    val size = 25.dp

    Button(
        onClick = { onClick() },
        modifier = Modifier
            .width(size)
            .height(size),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentRed1
        ),
        contentPadding = PaddingValues(5.dp)
    ) {
        Icon(
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            painter = painterResource
        )
    }
}