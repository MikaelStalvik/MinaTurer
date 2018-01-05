package com.imploded.minaturer.viewmodel

import com.google.gson.Gson
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.model.LocationContainer
import com.imploded.minaturer.model.LocationList
import com.imploded.minaturer.model.StopLocation
import com.imploded.minaturer.model.UiStop
import com.imploded.minaturer.repository.WebServiceRepository
import com.imploded.minaturer.utils.fromJson
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import java.util.ArrayList

class FindStopsViewModel(val settings: SettingsInterface) {

    var isSearching = false
    private val webservice: WebServiceInterface = WebServiceRepository()
    var locations: LocationContainer = LocationContainer(LocationList())
    private var filterString = ""

    fun updateFiltering(data: String): Boolean {
        if (data.length >= 3) {
            if (filterString != data) {
                filterString = data
                return true
            }
        }
        else {
            if (!filterString.isEmpty()) {
                filterString = ""
                return true
            }
        }
        return false
    }

    fun getStops(updateFun: (() -> Unit)) = async(UI) {
        isSearching = true
        val tokenTask = bg { webservice.getToken() }
        tokenTask.await()
        val searchTask = bg { webservice.getLocationsByName(filterString) }
        locations = searchTask.await()
        updateFun()
    }

    fun addStop(stop: StopLocation) {
        val activeSettings = settings.loadSettings()
        if (activeSettings.StopsList.isEmpty()) {
            val stops = listOf(UiStop(stop.name, stop.id, stop.lat, stop.lon))
            activeSettings.StopsList = Gson().toJson(stops)
        }
        else {
            val stops = Gson().fromJson<ArrayList<UiStop>>(activeSettings.StopsList)
            if (!stops.any { s -> s.id.equals(stop.id, true) }) {
                stops.add(UiStop(stop.name, stop.id, stop.lat, stop.lon))
                activeSettings.StopsList = Gson().toJson(stops)
            }
        }
        settings.saveSettings(activeSettings)
    }

}