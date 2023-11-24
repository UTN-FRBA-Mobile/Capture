package com.example.dadmapp.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.dadmapp.R
import com.example.dadmapp.model.tag.Tag
import com.example.dadmapp.ui.home.DropdownOption
import com.example.dadmapp.ui.theme.AccentRed1

@Composable
fun FilterMenu(
    toggleTag: (tag: Tag) -> Unit,
    allTags: List<Tag>,
    filterByTags: List<Tag>,
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        IconButton(onClick = { expanded = true }) {
            Icon(
                painterResource(id = R.drawable.filter),
                contentDescription = stringResource(R.string.FILTER_BY_TAGS),
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(AccentRed1)
        ) {
            allTags.forEach { t ->
                DropdownOption(
                    text = t.name,
                    onClick = { toggleTag(t) },
                    painterResource = if (filterByTags.contains(t)) {
                        painterResource(id = R.drawable.check)
                    } else { painterResource(id = R.drawable.square) },
                    contentDescription = t.name,
                )
            }
        }
    }
}