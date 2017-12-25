package com.imploded.trippinout.viewmodel

import android.util.Log
import com.imploded.trippinout.interfaces.WebServiceInterface
import com.imploded.trippinout.model.UiDeparture
import com.imploded.trippinout.repository.WebServiceRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class DeparturesViewModel {

    private val webservice: WebServiceInterface = WebServiceRepository()
    var uiDepartures: List<UiDeparture> = listOf()

    fun getDepartures(id: String) = async(UI) {
        val tokenTask = bg { webservice.getToken() }
        tokenTask.await()
        val searchTask = bg { webservice.getDepartures(id) }
        val departures = searchTask.await()

        uiDepartures = departures.departureBoard.departures.map { d ->
            UiDeparture(d.name, d.time, d.date, d.fgColor, d.bgColor)
        }
        Log.d("DEPARTURES", "COUNT:" + uiDepartures.count())
    }
}