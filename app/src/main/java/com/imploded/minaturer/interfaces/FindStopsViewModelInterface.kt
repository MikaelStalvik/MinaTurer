package com.imploded.minaturer.interfaces

import com.imploded.minaturer.model.LocationContainer
import com.imploded.minaturer.model.StopLocation
import kotlinx.coroutines.experimental.Deferred

interface FindStopsViewModelInterface {
    var isSearching: Boolean
    var locations: LocationContainer
    fun updateFiltering(data: String): Boolean
    fun getStops(updateFun: (() -> Unit)) : Deferred<Unit>
    fun addStop(stop: StopLocation)
}
