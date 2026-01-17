package com.example.myasdos

import android.app.Application
import com.example.myasdos.dependencies.AppContainer
import com.example.myasdos.dependencies.AppDataContainer

class MyAsdosApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}