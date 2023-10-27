package com.example.dadmapp.ui.signup


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
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

    val signUpViewModel : SignUpViewModel = viewModel(factory = SignUpViewModel.Factory)

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val allFieldsFilled = username.isNotBlank() && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTextField(label = "Username", value = username, onValueChange = { username = it })

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(label = "Email", value = email, onValueChange = { email = it })

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
            //viewModel.register(username, email, password, confirmPassword)
        }, enabled = allFieldsFilled)
    }
}