package com.imploded.minaturer.viewmodel

import com.imploded.minaturer.interfaces.SettingsInterface

class MainViewModel(private val settings: SettingsInterface) {

    fun shouldAskForRating() : Boolean {
        val activeSettings = settings.loadSettings()
        return ((!activeSettings.IsRated) and (activeSettings.StartCount > 0) and (activeSettings.StartCount.rem(5) == 0))
    }
    fun increaseStartCount() {
        val activeSettings = settings.loadSettings()
        activeSettings.StartCount = activeSettings.StartCount++
        settings.saveSettings(activeSettings)
    }

}