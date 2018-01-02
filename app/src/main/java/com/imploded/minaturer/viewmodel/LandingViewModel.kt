package com.imploded.minaturer.viewmodel

import com.google.gson.Gson
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.model.UiStop
import com.imploded.minaturer.utils.MinaTurerApp
import com.imploded.minaturer.utils.fromJson

class LandingViewModel(val settings: SettingsInterface) {

    var selectedStops: ArrayList<UiStop> = arrayListOf()

    fun getStops() {
        val activeSettings = settings.loadSettings()
        if (activeSettings.StopsList.isNotEmpty()) {
            selectedStops = Gson().fromJson<ArrayList<UiStop>>(activeSettings.StopsList)
        }
    }

    fun removeStop(index: Int) {
        //selectedStops.removeAt(index)
        val activeSettings = settings.loadSettings()
        var json = Gson().toJson(selectedStops)
        activeSettings.StopsList = json
        settings.saveSettings(activeSettings)
    }

}