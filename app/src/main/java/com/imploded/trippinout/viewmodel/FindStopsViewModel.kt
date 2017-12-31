package com.imploded.trippinout.viewmodel

import com.google.gson.Gson
import com.imploded.trippinout.interfaces.WebServiceInterface
import com.imploded.trippinout.model.LocationContainer
import com.imploded.trippinout.model.LocationList
import com.imploded.trippinout.model.StopLocation
import com.imploded.trippinout.model.UiStop
import com.imploded.trippinout.repository.WebServiceRepository
import com.imploded.trippinout.utils.TrippinOutApp
import com.imploded.trippinout.utils.fromJson
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import java.util.ArrayList

class FindStopsViewModel {

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
        var settings = TrippinOutApp.prefs.loadSettings()
        if (settings.StopsList.isEmpty()) {
            val stops = listOf(UiStop(stop.name, stop.id))
            settings.StopsList = Gson().toJson(stops)
        }
        else {
            var stops = Gson().fromJson<ArrayList<UiStop>>(settings.StopsList)
            if (!stops.any { s -> s.id.equals(stop.id, true) }) {
                stops.add(UiStop(stop.name, stop.id))
                settings.StopsList = Gson().toJson(stops)
            }
        }
        TrippinOutApp.prefs.saveSettings(settings)
    }

}