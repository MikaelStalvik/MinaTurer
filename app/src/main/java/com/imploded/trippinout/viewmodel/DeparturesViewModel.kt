package com.imploded.trippinout.viewmodel

import android.util.Log
import com.imploded.trippinout.interfaces.WebServiceInterface
import com.imploded.trippinout.model.Departure
import com.imploded.trippinout.model.FilteredDeparture
import com.imploded.trippinout.model.UiDeparture
import com.imploded.trippinout.repository.WebServiceRepository
import com.imploded.trippinout.utils.TrippinOutApp
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class DeparturesViewModel {

    private val webservice: WebServiceInterface = WebServiceRepository()
    var uiDepartures: List<UiDeparture> = listOf()

    private fun isMatch(departure: Departure, filterList: List<FilteredDeparture>) : Boolean {
        return filterList
                .any { f -> f.shortName.equals(departure.sname, true) && f.direction.equals(departure.direction, true) }
    }
    fun getDepartures(id: String, updateFun: (() -> Unit)) = async(UI) {
        val tokenTask = bg { webservice.getToken() }
        tokenTask.await()
        val searchTask = bg { webservice.getDepartures(id) }
        val departures = searchTask.await()

        val filtered = TrippinOutApp.prefs.filteredDeparturesByStopId(id)
        if (filtered.count() > 0) {
            uiDepartures = departures.departureBoard.departures
                    .filter { d -> isMatch(d, filtered) }
                    .map { d ->
                        UiDeparture(d.name, d.sname, d.time, d.date, d.fgColor, d.bgColor, d.stop, "eta: " + d.rtTime, d.direction, d.stopid)
                    }
        }
        else {
            uiDepartures = departures.departureBoard.departures
                    .map { d ->
                        UiDeparture(d.name, d.sname, d.time, d.date, d.fgColor, d.bgColor, d.stop, "eta: " + d.rtTime, d.direction, d.stopid)
                    }
        }
        updateFun()
        Log.d("DEPARTURES", "COUNT:" + uiDepartures.count())
    }
}