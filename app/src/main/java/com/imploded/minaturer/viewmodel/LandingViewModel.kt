package com.imploded.minaturer.viewmodel

import com.google.gson.Gson
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.model.UiStop
import com.imploded.minaturer.utils.fromJson

class LandingViewModel(val settings: SettingsInterface) {

    var selectedStops: ArrayList<UiStop> = arrayListOf()

    fun getStops() {
        val activeSettings = settings.loadSettings()
        if (activeSettings.StopsList.isNotEmpty()) {
            selectedStops = Gson().fromJson<ArrayList<UiStop>>(activeSettings.StopsList)
        }
    }

    fun removeStop(position: Int) {
        selectedStops.removeAt(position)
        val activeSettings = settings.loadSettings()
        val json = Gson().toJson(selectedStops)
        activeSettings.StopsList = json
        settings.saveSettings(activeSettings)
    }

}