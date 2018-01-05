package com.imploded.minaturer.viewmodel

import android.util.Log
import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.model.Stop
import com.imploded.minaturer.model.UiDeparture
import com.imploded.minaturer.repository.WebServiceRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class JourneyDetailViewModel(private val sourceRef: String, private val sourceLat: String, private val sourceLon: String) {

    private val webservice: WebServiceInterface = WebServiceRepository()

    var stops: List<Stop> = listOf()

    fun getJourneyDetails(updateFun: (() -> Unit)) = async(UI) {
        val tokenTask = bg { webservice.getToken() }
        tokenTask.await()
        val searchTask = bg { webservice.getJourneyDetails(sourceRef) }
        val jdet = searchTask.await()

        val index = jdet.journeyDetail.stops.indexOfFirst { s -> s.lat == sourceLat && s.lon == sourceLon }
        stops = jdet.journeyDetail.stops.subList(index, jdet.journeyDetail.stops.size)

        updateFun()
        /*
        var thisStopFound = false
        for(stop in jdet.journeyDetail.stops) {
            if (!thisStopFound) thisStopFound = stop.lat == stopLat && stop.lon == stopLon
            Log.d("STOP", stop.name + " " + stop.depTime + " " + thisStopFound)
        }*/
    }
}