package com.imploded.minaturer.viewmodel

import com.imploded.minaturer.interfaces.JourneyDetailViewModelInterface
import com.imploded.minaturer.model.Stop
import com.imploded.minaturer.model.UiDeparture
import com.imploded.minaturer.utils.timeDifference
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class JourneyDetailViewModel : JourneyDetailViewModelInterface {

    private lateinit var sourceStopId: String
    private lateinit var sourceDeparture: UiDeparture

    override fun setInputParameters(sourceStopId: String, departure: UiDeparture) {
        this.sourceStopId = sourceStopId
        this.sourceDeparture = departure
    }

    override var stops: List<Stop> = listOf()

    override fun getJourneyDetails(updateFun: (() -> Unit), initFetchFun: (() -> Unit)) = async(UI) {
        initFetchFun()
        val index = sourceDeparture.stops.indexOfFirst { s -> s.id == sourceStopId }
        stops = sourceDeparture.stops.subList(index, sourceDeparture.stops.size)
        var leapTime = 0
        for((stopIndex, stop) in stops.withIndex()) {
            if (stopIndex > 0) {
                val previous = stops[stopIndex-1]
                leapTime += if (stopIndex == 1) {
                    timeDifference(previous.depDate, previous.depTime, stop.arrDate, stop.arrTime)
                }
                else {
                    timeDifference(previous.arrDate, previous.arrTime, stop.arrDate, stop.arrTime)
                }

                stop.timeDiff = leapTime
            }
        }
        updateFun()
    }
}