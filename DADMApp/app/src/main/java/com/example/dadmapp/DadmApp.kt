package com.example.dadmapp

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dadmapp.ui.home.HomePage
import com.example.dadmapp.ui.home.HomePageViewModel
import com.example.dadmapp.ui.login.LoginPage
import com.example.dadmapp.ui.note.NotePage
import com.example.dadmapp.ui.recordAudio.RecordAudioPage
import com.example.dadmapp.ui.signup.SignUpPage
import com.example.dadmapp.ui.theme.BgDark

enum class RouteState(val title: String) {
    Login("Login"),
    Home("Home"),
    SignUp("SignUp")
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DadmApp(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        containerColor = BgDark
    ) { _ ->

        NavHost(
            navController = navController,
            startDestination = RouteState.Login.title,
            modifier = Modifier
                .fillMaxSize()
        ) {
            composable(RouteState.Login.title) {
                LoginPage(
                    onLogin = { navController.navigate(RouteState.Home.title) },
                    onNavigateToRegister = { navController.navigate(RouteState.SignUp.title) }
                )
            }

            composable(RouteState.SignUp.title) {
                SignUpPage(onSingUp = {
                    navController.navigate(RouteState.Home.title)
                })
            }

            composable(RouteState.Home.title) {
                val vm = viewModel<HomePageViewModel>(factory = HomePageViewModel.Factory)
                HomePage(
                    { noteId: String -> navController.navigate("note/$noteId") },
                    vm,
                    { navController.navigate("recordAudio") }
                )
            }

            composable(
                "note/{noteId}",
                arguments = listOf(navArgument("noteId") { type = NavType.StringType })
            ) {
                navController.currentBackStackEntry?.arguments?.getString("noteId")?.let {
                    NotePage(
                        onBackClick = { navController.navigate(RouteState.Home.title) },
                        noteId = it
                    )
                }
            }

            composable("recordAudio") {
                RecordAudioPage(
                    onCreatedNote = { noteId -> navController.navigate("note/$noteId") }
                )
            }
        }
    }
}

