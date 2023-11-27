package com.example.dadmapp.ui.tags

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dadmapp.R
import com.example.dadmapp.ui.theme.AccentRed1
import com.example.dadmapp.ui.theme.BgDark
import com.example.dadmapp.utils.hexColorToObj

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition",
    "UnrememberedMutableState"
)
@Composable
fun TagsColours(
    tagsColoursViewModel: TagsColoursViewModel = viewModel(factory = TagsColoursViewModel.Factory),
    onBack: () -> Unit
) {
    val tags = tagsColoursViewModel.tags?.value

    val squareSize = 20.dp

    var selectedTag by remember {
        mutableStateOf<String?>(null)
    }

    val edits = remember {
        mutableStateMapOf<String, String>()
    }

    Scaffold(
        containerColor = BgDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { tagsColoursViewModel.onSaveEdits(edits, onBack) }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.BACK_ICON),
                        tint = Color.White
                    )
                }
            }
        },
    ) {
        paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                start = 16.dp,
                end = 16.dp
            )
        ) {
            if (tags != null) {
                items(tags.size) {
                    idx ->
                        val tag = tags[idx]

                        var col = if (tag.colour != null) hexColorToObj(tag.colour) else AccentRed1

                        val editColor = edits[tag.name]
                        if (editColor != null) {
                            col = hexColorToObj(editColor)
                        }

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(tag.name, color = Color.White, fontSize = 20.sp)
                            Box(
                                Modifier
                                    .height(squareSize)
                                    .width(squareSize)
                                    .background(col)
                                    .clickable { selectedTag = tag.name }
                            )
                        }

                        if (idx != tags.size - 1) {
                            Divider(
                                color = AccentRed1,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)
                            )
                        }
                }
            }
        }
    }

    if (selectedTag != null) {
        ColourPicker(
            onCancel = { selectedTag = null },
            onColorChange = { newVal ->
                edits[selectedTag!!] = newVal
                selectedTag = null
                Log.d("INFO", edits.toString())
            }
        )
    }
}