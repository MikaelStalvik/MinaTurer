package com.imploded.minaturer.repository

import android.content.Context
import android.content.SharedPreferences
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.model.SettingsModel
import com.imploded.minaturer.utils.AppConstants

class SettingsRepository(context: Context) : SettingsInterface {


    override fun loadSettings(): SettingsModel {
        return SettingsModel(
                prefs.getString(attrStops, ""),
                prefs.getString(attrFilteredStops, ""),
                prefs.getString(attrFilteredLines, ""),
                prefs.getBoolean(attrLandingPageHintShown, false),
                prefs.getBoolean(attrFindStopHintShown, false),
                prefs.getBoolean(attrDeparturesHintShown, false)
        )
    }

    override fun saveSettings(settings: SettingsModel) {
        prefs.edit().putString(attrStops, settings.StopsList).apply()
        prefs.edit().putString(attrFilteredStops, settings.FilteredTripsByStopId).apply()
        prefs.edit().putString(attrFilteredLines, settings.FilteredLinesByStopId).apply()
        prefs.edit().putBoolean(attrLandingPageHintShown, settings.LandingHintPageShown).apply()
        prefs.edit().putBoolean(attrFindStopHintShown, settings.FindStopHintShown).apply()
        prefs.edit().putBoolean(attrDeparturesHintShown, settings.DeparturesHintShown).apply()
    }

    companion object {
        val attrStops = "stops"
        val attrFilteredStops = "filteredStops"
        val attrFilteredLines = "filteredLines"
        val attrLandingPageHintShown = "attrLandingPageHintShown"
        val attrFindStopHintShown = "attrFindStopHintShown"
        val attrDeparturesHintShown = "attrDeparturesHintShown"
    }
    private val prefs: SharedPreferences = context.getSharedPreferences(AppConstants.settingsFilename, 0)

}