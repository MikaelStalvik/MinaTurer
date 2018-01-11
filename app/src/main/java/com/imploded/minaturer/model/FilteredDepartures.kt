package com.imploded.minaturer.model

import com.google.gson.Gson
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.utils.fromJson

object FilteredDepartures {

    private var filteredMap: HashMap<String, ArrayList<FilteredDeparture>> = hashMapOf()

    fun loadData(settings: SettingsInterface) {
        val activeSettings = settings.loadSettings()
        if (activeSettings.FilteredTripsByStopId.isNotEmpty()) {
            filteredMap = Gson().fromJson<HashMap<String, ArrayList<FilteredDeparture>>>(activeSettings.FilteredTripsByStopId)
        }
    }

    fun saveData(settings: SettingsInterface) {
        val activeSettings = settings.loadSettings()
        val json = Gson().toJson(filteredMap)
        activeSettings?.FilteredTripsByStopId = json
        settings.saveSettings(activeSettings)
    }

    fun setTestData(data: HashMap<String, ArrayList<FilteredDeparture>>) {
        filteredMap = data
    }

    fun addFilteredTrip(stopId: String, shortName: String, direction: String) {
        if (filteredMap.containsKey(stopId)) {
            val filteredItemsByStop = filteredMap[stopId] as ArrayList<FilteredDeparture>
            val item = filteredItemsByStop.filter { p -> p.shortName.equals(shortName, true) && p.direction.equals(direction, true) }.any()
            if (!item) {
                filteredMap[stopId]!!.add(FilteredDeparture(shortName, direction))
            }
        }
        else {
            filteredMap[stopId] = arrayListOf(FilteredDeparture(shortName, direction))
        }
    }

    fun filterListForStop(stopId: String) : List<FilteredDeparture> {
        return if (filteredMap.containsKey(stopId))
        {
            filteredMap[stopId] as List<FilteredDeparture>
        }
        else {
            listOf()
        }
    }

    fun resetFilterForStop(stopId: String) {
        if (filteredMap.containsKey(stopId)) {
            filteredMap[stopId]!!.clear()
        }
    }

    fun filterCountForStop(stopId: String): Int {
        if (filteredMap.containsKey(stopId)) {
            return filteredMap[stopId]!!.count()
        }
        return 0
    }
}