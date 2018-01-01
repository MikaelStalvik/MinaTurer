package com.imploded.minaturer.model

import com.google.gson.Gson
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.utils.fromJson

object FilteredLines {

    var filteredMap: HashMap<String, ArrayList<String>> = hashMapOf()

    fun loadData(settings: SettingsInterface) {
        val activeSettings = settings.loadSettings()
        if (activeSettings.FilteredLinesByStopId.isNotEmpty()) {
            filteredMap = Gson().fromJson<HashMap<String, ArrayList<String>>>(activeSettings.FilteredLinesByStopId)
        }
    }

    fun saveData(settings: SettingsInterface) {
        val activeSettings = settings.loadSettings()
        val json = Gson().toJson(filteredMap)
        activeSettings.FilteredLinesByStopId = json
        settings.saveSettings(activeSettings)
    }

    fun addFilteredLine(stopId: String, shortName: String) {
        if (filteredMap.containsKey(stopId)) {
            var filteredItemsByStop = FilteredLines.filteredMap[stopId] as ArrayList<String>
            val exists = filteredItemsByStop.any { p -> p.equals(shortName, true) }
            if (!exists) {
                FilteredLines.filteredMap[stopId]!!.add(shortName)
            }
        }
        else {
            FilteredLines.filteredMap[stopId] = arrayListOf(shortName)
        }
    }

    fun filterlistForStop(stopId: String) : List<String> {
        if (filteredMap.containsKey(stopId))
        {
            return filteredMap[stopId] as List<String>
        }
        else {
            return listOf()
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