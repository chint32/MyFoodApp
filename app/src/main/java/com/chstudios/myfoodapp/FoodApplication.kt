package com.chstudios.myfoodapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoodApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}