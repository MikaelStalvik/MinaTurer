package com.imploded.minaturer.utils

import android.app.Application
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.model.FilteredDepartures
import com.imploded.minaturer.repository.SettingsRepository

class MinaTurerApp : Application() {

    companion object {
        lateinit var prefs: SettingsInterface
    }

    override fun onCreate() {
        prefs = SettingsRepository(applicationContext)

        FilteredDepartures.loadData(prefs)

        super.onCreate()
    }
}