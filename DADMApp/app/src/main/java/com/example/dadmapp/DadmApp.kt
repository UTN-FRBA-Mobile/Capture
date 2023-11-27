package com.example.dadmapp

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.dadmapp.ui.applicationOpened.ApplicationOpened
import com.example.dadmapp.ui.home.HomePage
import com.example.dadmapp.ui.home.HomePageViewModel
import com.example.dadmapp.ui.login.LoginPage
import com.example.dadmapp.ui.note.NotePage
import com.example.dadmapp.ui.recordAudio.RecordAudioPage
import com.example.dadmapp.ui.signup.SignUpPage
import com.example.dadmapp.ui.tags.TagsColours
import com.example.dadmapp.ui.theme.BgDark

enum class RouteState(val title: String) {
    ApplicationOpened("ApplicationOpened"),
    Login("Login?fatalError={fatalError}"),
    Home("Home"),
    SignUp("SignUp")
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DadmApp(
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        containerColor = BgDark
    ) { _ ->

        NavHost(
            navController = navController,
            startDestination = RouteState.ApplicationOpened.title,
            modifier = Modifier
                .fillMaxSize()
        ) {
            composable(RouteState.ApplicationOpened.title) {
                ApplicationOpened(
                    onLogin = { navController.navigate(RouteState.Home.title) },
                    onFailure = { navController.navigate(RouteState.Login.title) },
                    onFatalErrorHandler = {
                        navController.navigate(
                            RouteState.Login.title
                                .replace("{fatalError}", "true")
                        )
                    }
                )
            }

            composable(
                RouteState.Login.title,
                arguments = listOf(navArgument("fatalError") { defaultValue = "false" })
            ) {
                val s = it.arguments?.getString("fatalError")
                if (s != null) {
                    Log.d("INFO", s)
                    Log.d("INFO", s.toBoolean().toString())
                } else {
                    Log.d("INFO", "No arg")
                }
                LoginPage(
                    onLogin = { navController.navigate(RouteState.Home.title) },
                    onNavigateToRegister = { navController.navigate(RouteState.SignUp.title) },
                    fatalError = it.arguments?.getString("fatalError").toBoolean()
                )
            }

            composable(RouteState.SignUp.title) {
                SignUpPage(
                    { navController.navigate(RouteState.Home.title) },
                    { navController.navigate(RouteState.Login.title) }
                )
            }

            composable(RouteState.Home.title) {
                val vm = viewModel<HomePageViewModel>(factory = HomePageViewModel.Factory)
                HomePage(
                    { noteId: String -> navController.navigate("note/$noteId") },
                    vm,
                    { navController.navigate("recordAudio") },
                    { navController.navigate(RouteState.Login.title) },
                    { navController.navigate("tagsColours") }
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
                    onCreatedNote = { noteId -> navController.navigate("note/$noteId") },
                    onBack = { navController.navigate(RouteState.Home.title) }
                )
            }

            composable("tagsColours") {
                TagsColours(onBack = { navController.navigate(RouteState.Home.title) })
            }
        }
    }
}

