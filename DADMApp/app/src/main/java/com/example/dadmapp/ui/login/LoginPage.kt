package com.example.dadmapp.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dadmapp.R
import com.example.dadmapp.ui.components.CustomButton
import com.example.dadmapp.ui.components.CustomTextField

@Composable
fun LoginPage(
    onLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)

    if (viewModel.logged) {
        LaunchedEffect(Unit) {
            onLogin()
        }
    }

    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val allFieldsFilled = username.isNotBlank() && password.isNotBlank()

    var isLoading by remember {
        mutableStateOf(false)
    }

    if (viewModel.loginError != null) {
        isLoading = false
        AlertDialog(
            onDismissRequest = { viewModel.loginError = null },
            title = { Text(text = stringResource(R.string.LOGIN_ERROR_STR)) },
            text = { Text(text = viewModel.loginError ?: "") },
            confirmButton = {
                TextButton(onClick = { viewModel.loginError = null }) {
                    Text(stringResource(R.string.Ok))
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            CustomTextField(label = stringResource(R.string.USERNAME), value = username, onValueChange = { username = it })
        }
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            CustomTextField(
                label = stringResource(R.string.PASSWORD),
                value = password,
                onValueChange = { password = it },
                isPassword = true
            )
        }
        Row(
            modifier = Modifier.padding(2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            CustomButton(label = stringResource(R.string.LOGIN), onClick = {
                isLoading = true
                viewModel.login(username, password)
            }, enabled = allFieldsFilled, showLoading = isLoading)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.OR_SIGN_UP),
                color = Color.White,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
    }
}