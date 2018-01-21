package com.imploded.minaturer.viewmodel

import com.imploded.minaturer.interfaces.JourneyDetailViewModelInterface
import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.model.Stop
import com.imploded.minaturer.model.UiDeparture
import com.imploded.minaturer.utils.timeDifference
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class JourneyDetailViewModel(private val webservice: WebServiceInterface) : JourneyDetailViewModelInterface {

    private lateinit var sourceRef: String
    private lateinit var sourceStopId: String
    private lateinit var sourceDeparture: UiDeparture

    override fun setInputParameters(sourceRef: String, sourceStopId: String, departure: UiDeparture) {
        this.sourceRef = sourceRef
        this.sourceStopId = sourceStopId
        this.sourceDeparture = departure
    }

    override var stops: List<Stop> = listOf()

    override fun getJourneyDetails(updateFun: (() -> Unit), initFetchFun: (() -> Unit)) = async(UI) {
        initFetchFun()
        /*
        val tokenTask = bg { webservice.getToken() }
        tokenTask.await()
        val searchTask = bg { webservice.getJourneyDetails(sourceRef) }
        val jdet = searchTask.await()

        val index = jdet.journeyDetail.stops.indexOfFirst { s -> s.id == sourceStopId }
        stops = jdet.journeyDetail.stops.subList(index, jdet.journeyDetail.stops.size)
        var leapTime = 0
        for((stopIndex, stop) in stops.withIndex()) {
            if (stopIndex > 0) {
                val previous = stops[stopIndex-1]
                leapTime += if (stopIndex < stops.size-1) {
                    timeDifference(previous.depDate, previous.depTime, stop.depDate, stop.depTime)
                } else {
                    timeDifference(previous.depDate, previous.depTime, stop.arrDate, stop.arrTime)
                }
                stop.timeDiff = leapTime
            }
        }
        */
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