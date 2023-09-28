package com.example.dadmapp

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dadmapp.ui.home.HomePage
import com.example.dadmapp.ui.login.LoginPage
import com.example.dadmapp.ui.theme.BgDark

enum class RouteState(val title: String) {
    Login("Login"),
    Home("Home")
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DadmApp(
    navController: NavHostController = rememberNavController()
) {

    Scaffold{
        _ ->

        NavHost(
            navController = navController,
            startDestination = RouteState.Login.title,
            modifier = Modifier
                .fillMaxSize()
                .background(BgDark)
        ) {
            composable(route = RouteState.Login.title) {
                LoginPage(onLogin = { navController.navigate(RouteState.Home.title) })
            }
            
            composable(route = RouteState.Home.title) {
                HomePage()
            }
        }
    }
}