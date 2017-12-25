package com.imploded.trippinout.repository

import android.content.Context
import android.content.SharedPreferences
import com.imploded.trippinout.interfaces.SettingsInterface
import com.imploded.trippinout.model.SettingsModel
import com.imploded.trippinout.ui.AppConstants

class SettingsRepository(context: Context) : SettingsInterface {

    override fun loadSettings(): SettingsModel {
        return SettingsModel(
                prefs.getString(attrStops, "")
        )
    }

    override fun saveSettings(settings: SettingsModel) {
        prefs.edit().putString(attrStops, settings.StopsList).apply()
    }

    companion object {
        val attrStops = "stops"
    }
    private val prefs: SharedPreferences = context.getSharedPreferences(AppConstants.settingsFilename, 0)

}