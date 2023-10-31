package com.example.dadmapp.ui.signup


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
fun SignUpPage(
    onSingUp: () -> Unit
) {

    val viewModel : SignUpViewModel = viewModel(factory = SignUpViewModel.Factory)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember {
        mutableStateOf(false)
    }

    val allFieldsFilled = username.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()

    // Show an alert dialog if there is a registration error
    if (viewModel.registeredError != null) {
        isLoading = false
        AlertDialog(
            onDismissRequest = { viewModel.registeredError = null },
            title = { Text(text = "Registration Error") },
            text = { Text(text = viewModel.registeredError ?: "") },
            confirmButton = {
                TextButton(onClick = { viewModel.registeredError = null }) {
                    Text("OK")
                }
            }
        )
    }

    // Observe the registered state and navigate to the home page if the user is registered
    val isRegistered by viewModel.registered.collectAsState()
    LaunchedEffect(isRegistered) {
        if (viewModel.registered.value) {
            onSingUp()
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTextField(label = "Username", value = username, onValueChange = { username = it })

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            isPassword = true,
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            label = "Confirm Password",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            isPassword = true,
        )

        Spacer(modifier = Modifier.height(32.dp))

        CustomButton(label = "Register", onClick = {
            viewModel.register(username, password, confirmPassword)
            isLoading = true
        }, enabled = allFieldsFilled, showLoading = isLoading)
    }
}