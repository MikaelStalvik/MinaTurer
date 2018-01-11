package com.imploded.minaturer.interfaces

import com.imploded.minaturer.model.Stop
import kotlinx.coroutines.experimental.Deferred

interface JourneyDetailViewModelInterface {
    var stops: List<Stop>
    fun setInputParameters(sourceRef: String, sourceStopId: String)
    fun getJourneyDetails(updateFun: (() -> Unit), initFetchFun: (() -> Unit)) : Deferred<Unit>
}
