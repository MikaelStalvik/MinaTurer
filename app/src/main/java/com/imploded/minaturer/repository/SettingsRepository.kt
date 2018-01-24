package com.imploded.minaturer.repository

import android.content.Context
import android.content.SharedPreferences
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.model.SettingsModel
import com.imploded.minaturer.utils.AppConstants

class SettingsRepository(context: Context) : SettingsInterface {

    private val defaultFilters = AppConstants.Subway + AppConstants.Tram + AppConstants.Bus

    override fun loadSettings(): SettingsModel {
        return SettingsModel(
                prefs.getString(attrStops, ""),
                prefs.getString(attrFilteredStops, ""),
                prefs.getString(attrFilteredLines, ""),
                prefs.getBoolean(attrLandingPageHintShown, false),
                prefs.getBoolean(attrFindStopHintShown, false),
                prefs.getBoolean(attrDeparturesHintShown, false),
                prefs.getBoolean(attrIsRated, false),
                prefs.getInt(attrStartCount, 0),
                prefs.getInt(attrFilters, defaultFilters)
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
        prefs.edit().putInt(attrFilters, settings.activeFilters).apply()
    }

    companion object {
        const val attrStops = "stops2"
        const val attrFilteredStops = "filteredStops2"
        const val attrFilteredLines = "filteredLines2"
        const val attrLandingPageHintShown = "attrLandingPageHintShown"
        const val attrFindStopHintShown = "attrFindStopHintShown"
        const val attrDeparturesHintShown = "attrDeparturesHintShown"
        const val attrIsRated = "attrIsRated"
        const val attrStartCount = "attrStartCount"
        const val attrFilters = "attrFilters"
    }
    private val prefs: SharedPreferences = context.getSharedPreferences(AppConstants.settingsFilename, 0)

}