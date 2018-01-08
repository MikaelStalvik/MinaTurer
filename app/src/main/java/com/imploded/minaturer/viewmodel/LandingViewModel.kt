package com.imploded.minaturer.viewmodel

import com.google.gson.Gson
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.model.UiStop
import com.imploded.minaturer.utils.fromJson

interface LandingViewModelInterface {
    var selectedStops: ArrayList<UiStop>
    fun getStops()
    fun removeStop(position: Int)
}

class LandingViewModel(val settings: SettingsInterface) : LandingViewModelInterface {

    override var selectedStops: ArrayList<UiStop> = arrayListOf()

    override fun getStops() {
        val activeSettings = settings.loadSettings()
        if (activeSettings.StopsList.isNotEmpty()) {
            selectedStops = Gson().fromJson<ArrayList<UiStop>>(activeSettings.StopsList)
        }
    }

    override fun removeStop(position: Int) {
        selectedStops.removeAt(position)
        val activeSettings = settings.loadSettings()
        val json = Gson().toJson(selectedStops)
        activeSettings.StopsList = json
        settings.saveSettings(activeSettings)
    }

}