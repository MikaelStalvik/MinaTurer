package com.imploded.minaturer.interfaces

import com.imploded.minaturer.model.Stop
import com.imploded.minaturer.model.UiDeparture
import kotlinx.coroutines.experimental.Deferred

interface JourneyDetailViewModelInterface {
    var stops: List<Stop>
    fun setInputParameters(sourceStopId: String, departure: UiDeparture)
    fun getJourneyDetails(updateFun: (() -> Unit), initFetchFun: (() -> Unit)) : Deferred<Unit>
}
