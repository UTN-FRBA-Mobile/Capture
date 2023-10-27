package com.example.dadmapp.ui.components

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.dadmapp.AUDIO_PATH
import com.example.dadmapp.LOCALHOST_URL
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
    val url = "$LOCALHOST_URL/$AUDIO_PATH/$audioName"

    val mediaPlayer = MediaPlayer()
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
    }

    if (!loaded) {
        return
    }

    if (playing) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(1000)
                val newVal = currentValue + 1000

                if (newVal > duration) {
                    currentValue = duration
                } else {
                    currentValue += 1000
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
            .padding(5.dp)
    ) {
        if (playing) {
            IconButton(
                onClick = {
                    playing = false
                    mediaPlayer.stop()
                },
                icon = Icons.Filled.Warning,
                contentDescription = "Stop audio"
            )
        } else {
            IconButton(
                onClick = {
                    playing = true
                    mediaPlayer.start()
                },
                icon = Icons.Filled.PlayArrow,
                contentDescription = "Play audio"
            )
        }
        
        if (playing) {
            Text(text = msToMinutesAndSeconds(currentValue))
        } else {
            Text(text = msToMinutesAndSeconds(duration))
        }
    }
}

@Composable
fun IconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String
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