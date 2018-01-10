package com.imploded.minaturer.interfaces

import com.imploded.minaturer.model.DepartureContainer
import com.imploded.minaturer.model.UiDeparture
import kotlinx.coroutines.experimental.Deferred

interface DeparturesViewModelInterface {
    var uiDepartures: List<UiDeparture>
    val filterActive: Boolean
    fun setStopId(stopId: String)
    fun toggleFilterMode()
    fun generateFilteredDepartures(departures: DepartureContainer)
    fun getDepartures(stopId: String, updateFun: (() -> Unit)): Deferred<Unit>
    fun selectAll()
    fun selectNone()
    fun filtersActive(): Boolean
    fun resetFilterForStop()
    fun applyFilters()
}