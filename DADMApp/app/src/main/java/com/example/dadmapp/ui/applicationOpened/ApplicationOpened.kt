package com.example.dadmapp.ui.applicationOpened

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dadmapp.data.FatalErrorHandler
import com.example.dadmapp.ui.theme.LightRed

@Composable
fun ApplicationOpened(
    applicationOpenedViewmodel: ApplicationOpenedViewmodel = viewModel(factory = ApplicationOpenedViewmodel.Factory),
    onLogin: () -> Unit,
    onFailure: () -> Unit,
    onFatalErrorHandler: () -> Unit
) {
    applicationOpenedViewmodel.setFatalErrorHandler(onFatalErrorHandler)

    LaunchedEffect(Unit) {
        applicationOpenedViewmodel.tryToLoadNotes()
    }

    if (applicationOpenedViewmodel.loginState == LoginState.LOGGED.stateName) {
        onLogin()
    }

    if (applicationOpenedViewmodel.loginState == LoginState.NOT_LOGGED_BEFORE.stateName) {
        onFailure()
    }

    if (applicationOpenedViewmodel.loginState == LoginState.FAILURE.stateName) {
        AlertDialog(
            onDismissRequest = { onFailure() },
            title = { Text(text = "Login error") },
            text = { Text(text = "Your session expired or there was an error. Please log in again") },
            confirmButton = {
                TextButton(onClick = { onFailure() }) {
                    Text("Go to login")
                }
            }
        )
    }

    val size = 100.dp

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = LightRed,
            modifier = Modifier
                .width(size)
                .height(size)
        )
    }
}