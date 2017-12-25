package com.imploded.trippinout.viewmodel

import com.imploded.trippinout.interfaces.WebServiceInterface
import com.imploded.trippinout.model.LocationContainer
import com.imploded.trippinout.model.LocationList
import com.imploded.trippinout.repository.WebServiceRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

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

    fun getDepartures(id: String) = async(UI) {
        val tokenTask = bg { webservice.getToken() }
        tokenTask.await()
        val searchTask = bg { webservice.getDepartures(id) }
        val departures = searchTask.await()
        val cn = departures.departureBoard.departures.count()
        val k = 123
    }
}