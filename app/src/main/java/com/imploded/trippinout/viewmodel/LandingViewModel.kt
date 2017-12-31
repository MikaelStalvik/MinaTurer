package com.imploded.trippinout.viewmodel

import com.google.gson.Gson
import com.imploded.trippinout.model.UiStop
import com.imploded.trippinout.utils.TrippinOutApp
import com.imploded.trippinout.utils.fromJson

class LandingViewModel {
    var selectedStops: ArrayList<UiStop> = arrayListOf()

    fun getStops() {
        val settings = TrippinOutApp.prefs.loadSettings()
        if (settings.StopsList.isNotEmpty()) {
            selectedStops = Gson().fromJson<ArrayList<UiStop>>(settings.StopsList)
        }
    }
}