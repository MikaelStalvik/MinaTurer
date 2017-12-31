package com.imploded.trippinout.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.imploded.trippinout.interfaces.SettingsInterface
import com.imploded.trippinout.model.FilteredDeparture
import com.imploded.trippinout.model.SettingsModel
import com.imploded.trippinout.utils.AppConstants
import com.imploded.trippinout.utils.fromJson

class SettingsRepository(context: Context) : SettingsInterface {

    override fun filteredDeparturesByStopId(stopId: String): ArrayList<FilteredDeparture> {
        val json = prefs.getString(stopId, "")
        if (json.isEmpty()) {
            return arrayListOf()
        }
        else {
            val data = Gson().fromJson<ArrayList<FilteredDeparture>>(json)
            val c = data.count()
            Log.d("CNT", data.count().toString())
            return data
        }
    }

    override fun setFilteredDeparturesByStopId(stopId: String, data: List<FilteredDeparture>) {
        val json = Gson().toJson(data)
        prefs.edit().putString(stopId, json).apply()
    }

    override fun loadSettings(): SettingsModel {
        return SettingsModel(
                prefs.getString(attrStops, ""),
                prefs.getString(attrFilteredStops, "")
        )
    }

    override fun saveSettings(settings: SettingsModel) {
        prefs.edit().putString(attrStops, settings.StopsList).apply()
        prefs.edit().putString(attrFilteredStops, settings.FilteredTripsByStopId).apply()
    }

    companion object {
        val attrStops = "stops"
        val attrFilteredStops = "filteredStops"
    }
    private val prefs: SharedPreferences = context.getSharedPreferences(AppConstants.settingsFilename, 0)

}