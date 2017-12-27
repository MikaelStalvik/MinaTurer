package com.imploded.trippinout.interfaces

import com.imploded.trippinout.model.FilteredDeparture
import com.imploded.trippinout.model.SettingsModel

interface SettingsInterface {

    fun loadSettings(): SettingsModel
    fun saveSettings(settings: SettingsModel)
    fun filteredDeparturesByStopId(stopId: String): ArrayList<FilteredDeparture>
    fun setFilteredDeparturesByStopId(stopId: String, data: List<FilteredDeparture>)
}