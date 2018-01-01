package com.imploded.minaturer.viewmodel

import com.google.gson.Gson
import com.imploded.minaturer.model.UiStop
import com.imploded.minaturer.utils.MinaTurerApp
import com.imploded.minaturer.utils.fromJson

class LandingViewModel {
    var selectedStops: ArrayList<UiStop> = arrayListOf()

    fun getStops() {
        val settings = MinaTurerApp.prefs.loadSettings()
        if (settings.StopsList.isNotEmpty()) {
            selectedStops = Gson().fromJson<ArrayList<UiStop>>(settings.StopsList)
        }
    }
}