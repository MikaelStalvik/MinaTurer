package com.imploded.minaturer.viewmodel

import com.imploded.minaturer.interfaces.DeparturesViewModelInterface
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.model.*
import com.imploded.minaturer.utils.etaTime
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class DeparturesViewModel(private val webservice: WebServiceInterface, private val settings: SettingsInterface) : DeparturesViewModelInterface {

    override fun setStopId(stopId: String) {
        this.stopId = stopId
    }
    private var stopId: String = ""

    private var filterMode = false
    private fun itemIsFiltered(departure: Departure, filterList: List<FilteredDeparture>) : Boolean {
        return filterList
                .any { f -> f.shortName.equals(departure.sname, true) && f.direction.equals(departure.direction, true) }
    }

    override val filterActive: Boolean
        get() = filterMode
    override fun toggleFilterMode() {
        filterMode = !filterMode
    }

    override var uiDepartures: List<UiDeparture> = listOf()

    override fun generateFilteredDepartures(departures: DepartureContainer) {
        val uniqueDepartures = departures.departureBoard.departures.distinctBy { Pair(it.sname, it.direction) }
        val filtered = FilteredDepartures.filterListForStop(stopId)
        uiDepartures = if (filtered.count() > 0) {
            uniqueDepartures
                    .filter { d -> !itemIsFiltered(d, filtered) }
                    .map { d ->
                        UiDeparture(d.name, d.sname, d.time, d.date, d.fgColor, d.bgColor, d.stop, d.rtTime?.etaTime(d.time), d.direction, d.stopid, true, JourneyRef(d.journeyRefIds.ref))
                    }
        } else {
            uniqueDepartures
                    .map { d ->
                        UiDeparture(d.name, d.sname, d.time, d.date, d.fgColor, d.bgColor, d.stop, d.rtTime?.etaTime(d.time), d.direction, d.stopid, true, JourneyRef(d.journeyRefIds.ref))
                    }
        }
        for((index, departure) in uiDepartures.withIndex()) departure.index = index
    }

    override fun getDepartures(stopId: String, updateFun: (() -> Unit), initFetchFun: (() -> Unit)) = async(UI) {
        initFetchFun()
        val tokenTask = bg { webservice.getToken() }
        tokenTask.await()
        val searchTask = bg { webservice.getDepartures(stopId) }
        val departures = searchTask.await()

        generateFilteredDepartures(departures)

        updateFun()
    }

    override fun selectAll() {
        for(item in uiDepartures) item.checked = true
    }
    override fun selectNone() {
        for(item in uiDepartures) item.checked = false
    }

    override fun filtersActive(): Boolean = FilteredDepartures.filterCountForStop(stopId)> 0

    override fun resetFilterForStop() {
        FilteredDepartures.resetFilterForStop(stopId)
    }

    override fun applyFilters() {
        uiDepartures
                .filter { !it.checked }
                .forEach { FilteredDepartures.addFilteredTrip(stopId, it.shortName, it.direction) }
        FilteredDepartures.saveData(settings)
    }

}