package com.imploded.minaturer.interfaces

import com.imploded.minaturer.model.SettingsModel

interface SettingsInterface {

    fun loadSettings(): SettingsModel
    fun saveSettings(settings: SettingsModel)
}