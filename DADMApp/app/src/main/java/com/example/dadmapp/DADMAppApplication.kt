package com.example.dadmapp

import android.app.Application
import com.example.dadmapp.data.AppContainer
import com.example.dadmapp.data.DefaultAppContainer

class DADMAppApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}