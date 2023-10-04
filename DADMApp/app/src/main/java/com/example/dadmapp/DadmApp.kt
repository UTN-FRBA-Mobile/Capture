package com.example.dadmapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dadmapp.ui.home.HomePage
import com.example.dadmapp.ui.login.LoginPage
import com.example.dadmapp.ui.theme.BgDark

enum class RouteState(val title: String) {
    Login("Login"),
    Home("Home"),
    ViewNote("ViewNote")
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DadmApp(
    navController: NavHostController = rememberNavController()
) {

    Scaffold(
        containerColor = BgDark
    ) {
        _ ->

        NavHost(
            navController = navController,
            startDestination = RouteState.Login.title,
            modifier = Modifier
                .fillMaxSize()
        ) {
            composable(RouteState.Login.title) {
                LoginPage(onLogin = { navController.navigate(RouteState.Home.title) })
            }
            
            composable(RouteState.Home.title) {
                HomePage(onNoteClick = { noteId: String -> navController.navigate("note/$noteId") })
            }

            composable(
                "note/{noteId}",
                arguments = listOf(navArgument("noteId") { type = NavType.StringType })
            ) {
                backStackEntry ->
                Text("Note ID is " + backStackEntry.arguments?.getString("noteId"))
            }
        }
    }
}

