package com.example.dadmapp.ui.signup


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dadmapp.R
import com.example.dadmapp.ui.components.CustomButton
import com.example.dadmapp.ui.components.CustomTextField
import com.example.dadmapp.ui.theme.LightRed

@Composable
fun GetText(state: String) {
   val text = when (state) {
       SignupError.UsernameExists.title -> stringResource(R.string.USERNAME_ALREADY_EXISTS)
       SignupError.PasswordsDoNotMatch.title -> stringResource(R.string.PASSWORDS_DO_NOT_MATCH)
       else -> stringResource(R.string.FATAL_ERROR_TEXT)
   }

    Text(text)
}

@Composable
fun SignUpPage(
    onSingUp: () -> Unit,
    onNavigateToLogin: () -> Unit
) {

    val viewModel : SignUpViewModel = viewModel(factory = SignUpViewModel.Factory)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember {
        mutableStateOf(false)
    }

    val allFieldsFilled = username.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()

    if (viewModel.signupError != null) {
        isLoading = false
        AlertDialog(
            onDismissRequest = { viewModel.signupError = null },
            title = { Text(text = stringResource(id = R.string.SIGNUP_ERROR)) },
            text = { GetText(viewModel.signupError!!) },
            confirmButton = {
                TextButton(onClick = { viewModel.signupError = null }) {
                    Text("OK")
                }
            }
        )
    }

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

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomButton(label = "Register", onClick = {
                viewModel.register(username, password, confirmPassword)
                isLoading = true
            }, enabled = allFieldsFilled, showLoading = isLoading)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.OR) + " ",
                color = Color.White,
            )
            Text(
                text = stringResource(R.string.LOGIN).lowercase(),
                color = LightRed,
                modifier = Modifier.clickable { onNavigateToLogin() },
            )
        }
    }
}