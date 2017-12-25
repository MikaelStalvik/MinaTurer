package com.imploded.trippinout.utils

import android.app.Application
import com.imploded.trippinout.interfaces.SettingsInterface
import com.imploded.trippinout.repository.SettingsRepository

class TrippinOutApp : Application() {

    companion object {
        lateinit var prefs: SettingsInterface
    }

    override fun onCreate() {
        prefs = SettingsRepository(applicationContext)
        super.onCreate()
    }
}