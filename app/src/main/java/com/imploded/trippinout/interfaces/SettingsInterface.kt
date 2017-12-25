package com.imploded.trippinout.interfaces

import com.imploded.trippinout.model.SettingsModel

interface SettingsInterface {

    fun loadSettings(): SettingsModel
    fun saveSettings(settings: SettingsModel)
}