package com.imploded.trippinout.viewmodel

import com.google.gson.Gson
import com.imploded.trippinout.model.UiStop
import com.imploded.trippinout.utils.TrippinOutApp
import com.imploded.trippinout.utils.fromJson

class LandingViewModel {
    var selectedStops: List<UiStop> = listOf()

    fun getStops() {
        val settings = TrippinOutApp.prefs.loadSettings()
        selectedStops = Gson().fromJson<List<UiStop>>(settings.StopsList)

    }
}