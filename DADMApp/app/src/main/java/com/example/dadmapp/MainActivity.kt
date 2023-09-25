package com.example.dadmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.dadmapp.ui.theme.BgDark
import com.example.dadmapp.ui.theme.DADMAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DADMAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    DadmApp()
                }
            }
        }
    }
}