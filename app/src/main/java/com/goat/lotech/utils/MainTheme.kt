package com.goat.lotech.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MainTheme: Application() {

    override fun onCreate() {
        super.onCreate()
  
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME)
    }
}