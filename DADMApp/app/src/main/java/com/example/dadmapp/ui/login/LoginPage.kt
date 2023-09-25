package com.example.dadmapp.ui.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dadmapp.ui.components.CustomButton
import com.example.dadmapp.ui.components.CustomTextField

@Composable
fun LoginPage(
    onLogin: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)

    if (viewModel.logged) {
        LaunchedEffect(Unit) {
            Log.i("INFO", "\n\nACA\n\n")
            onLogin()
        }
    }

    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            CustomTextField(label = "Username", value = username, onValueChange = { username = it })
        }
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            CustomTextField(label = "Password", value = password, onValueChange = { password = it })
        }
        Row(
            modifier = Modifier.padding(2.dp)
        ) {
            CustomButton(label = "Iniciar sesion", onClick = { viewModel.login(username, password) })
        }
    }
}