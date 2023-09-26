package com.example.dadmapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.dadmapp.data.AppContainer
import com.example.dadmapp.data.DefaultAppContainer

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)
class DADMAppApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext.dataStore)
    }
}