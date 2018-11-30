package com.nasri.tutorial.Controller

import android.app.Application
import com.nasri.tutorial.Utilities.SharedPrefs

class App : Application() {

    companion object {
        lateinit var prefs : SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}