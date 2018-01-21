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
                prefs.getBoolean(attrDeparturesHintShown, false),
                prefs.getBoolean(attrIsRated, false),
                prefs.getInt(attrStartCount, 0)
        )
    }

    override fun saveSettings(settings: SettingsModel) {
        prefs.edit().putString(attrStops, settings.StopsList).apply()
        prefs.edit().putString(attrFilteredStops, settings.FilteredTripsByStopId).apply()
        prefs.edit().putString(attrFilteredLines, settings.FilteredLinesByStopId).apply()
        prefs.edit().putBoolean(attrLandingPageHintShown, settings.LandingHintPageShown).apply()
        prefs.edit().putBoolean(attrFindStopHintShown, settings.FindStopHintShown).apply()
        prefs.edit().putBoolean(attrDeparturesHintShown, settings.DeparturesHintShown).apply()
        prefs.edit().putBoolean(attrIsRated, settings.IsRated).apply()
        prefs.edit().putInt(attrStartCount, settings.StartCount).apply()
    }

    companion object {
        const val attrStops = "stops"
        const val attrFilteredStops = "filteredStops"
        const val attrFilteredLines = "filteredLines"
        const val attrLandingPageHintShown = "attrLandingPageHintShown"
        const val attrFindStopHintShown = "attrFindStopHintShown"
        const val attrDeparturesHintShown = "attrDeparturesHintShown"
        const val attrIsRated = "attrIsRated"
        const val attrStartCount = "attrStartCount"
    }
    private val prefs: SharedPreferences = context.getSharedPreferences(AppConstants.settingsFilename, 0)

}