package com.example.dadmapp.ui.home.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dadmapp.R
import com.example.dadmapp.model.tag.Tag
import com.example.dadmapp.ui.home.HomePageViewModel
import com.example.dadmapp.ui.theme.AccentRed1
import com.example.dadmapp.ui.theme.BgDark


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    homePageViewModel: HomePageViewModel,
    onDrawerStateChange: () -> Unit
) {
    val allTags = homePageViewModel.tags?.collectAsState()

    fun toggleTag(tag: Tag) {
        homePageViewModel.filterByTags = if (homePageViewModel.filterByTags.contains(tag)) {
            homePageViewModel.filterByTags.filterNot { it == tag }
        } else {
            homePageViewModel.filterByTags.plus(tag)
        }
    }

    var isSearchBarVisible by remember {
        mutableStateOf(false)
    }

    Crossfade(
        targetState = isSearchBarVisible,
        animationSpec = tween(350),
        label = ""
    ) {
        if (it) {
            TopSearchBar(
                onCloseIcon = {
                    isSearchBarVisible = false
                    homePageViewModel.searchTerm = null
                },
                homePageViewModel
            )
        } else {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(
                        onClick = { onDrawerStateChange() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(R.string.BURGER_MENU_BUTTON),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { isSearchBarVisible = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(R.string.SEARCH_BUTTON),
                            tint = Color.LightGray
                        )
                    }
                    FilterMenu(
                        // allTags = allTags.value,
                        allTags = allTags?.value ?: emptyList(),
                        filterByTags = homePageViewModel.filterByTags,
                        toggleTag = { tag -> toggleTag(tag) },
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(45.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(BgDark),
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = AccentRed1,
                    titleContentColor = BgDark
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
    onCloseIcon: () -> Unit,
    homePageViewModel: HomePageViewModel
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .fillMaxWidth()
            .requiredHeight(50.dp),
        value = homePageViewModel.searchTerm ?: "",
        onValueChange = {
            homePageViewModel.searchTerm = it
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.SEARCH_ICON),
                tint = Color.LightGray
            )
        },
        trailingIcon = {
            IconButton(onClick = { onCloseIcon() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.CLOSE_ICON),
                    tint = Color.White
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = AccentRed1,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.Gray
        )
    )
}