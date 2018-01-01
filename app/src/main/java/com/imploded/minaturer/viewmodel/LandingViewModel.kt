package com.imploded.minaturer.viewmodel

import com.google.gson.Gson
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.model.UiStop
import com.imploded.minaturer.utils.MinaTurerApp
import com.imploded.minaturer.utils.fromJson

class LandingViewModel(val settings: SettingsInterface) {

    var selectedStops: ArrayList<UiStop> = arrayListOf()

    fun getStops() {
        //val settings = MinaTurerApp.prefs.loadSettings()
        val activeSettings = settings.loadSettings()
        if (activeSettings.StopsList.isNotEmpty()) {
            selectedStops = Gson().fromJson<ArrayList<UiStop>>(activeSettings.StopsList)
        }
    }

    fun storeStops(stops: List<UiStop>) {
        val activeSettings = settings.loadSettings()
        var json = Gson().toJson(stops)
        activeSettings.StopsList = json
        settings.saveSettings(activeSettings)
    }
}