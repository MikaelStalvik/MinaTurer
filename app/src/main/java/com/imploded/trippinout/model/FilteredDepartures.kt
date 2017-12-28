package com.imploded.trippinout.model

import com.google.gson.Gson
import com.imploded.trippinout.interfaces.SettingsInterface
import com.imploded.trippinout.utils.fromJson

object FilteredDepartures {

    var filteredMap: HashMap<String, ArrayList<FilteredDeparture>> = hashMapOf()

    fun loadData(settings: SettingsInterface) {
        val activeSettings = settings.loadSettings()
        if (activeSettings.FilteredTripsByStopId.isNotEmpty()) {
            filteredMap = Gson().fromJson<HashMap<String, ArrayList<FilteredDeparture>>>(activeSettings.FilteredTripsByStopId)
        }
    }

    fun saveData(settings: SettingsInterface) {
        val activeSettings = settings.loadSettings()
        val json = Gson().toJson(filteredMap)
        activeSettings.FilteredTripsByStopId = json
        settings.saveSettings(activeSettings)
    }

    fun addFilteredTrip(stopId: String, shortName: String, direction: String) {
        if (filteredMap.containsKey(stopId)) {
            var filteredItemsByStop = filteredMap[stopId] as ArrayList<FilteredDeparture>
            val item = filteredItemsByStop.filter { p -> p.shortName.equals(shortName, true) && p.direction.equals(direction, true) }.any()
            if (!item) {
                filteredMap[stopId]!!.add(FilteredDeparture(shortName, direction))
            }
        }
        else {
            filteredMap[stopId] = arrayListOf(FilteredDeparture(shortName, direction))
        }
    }

    fun filterlistForStop(stopId: String) : List<FilteredDeparture> {
        if (filteredMap.containsKey(stopId))
        {
            return filteredMap[stopId] as List<FilteredDeparture>
        }
        else {
            return listOf()
        }
    }


}