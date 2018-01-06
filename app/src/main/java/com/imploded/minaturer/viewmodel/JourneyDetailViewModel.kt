package com.imploded.minaturer.viewmodel

import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.model.Stop
import com.imploded.minaturer.repository.WebServiceRepository
import com.imploded.minaturer.utils.timeDifference
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class JourneyDetailViewModel(private val sourceRef: String, private val sourceStopId: String) {

    private val webservice: WebServiceInterface = WebServiceRepository()

    var stops: List<Stop> = listOf()

    fun getJourneyDetails(updateFun: (() -> Unit)) = async(UI) {
        val tokenTask = bg { webservice.getToken() }
        tokenTask.await()
        val searchTask = bg { webservice.getJourneyDetails(sourceRef) }
        val jdet = searchTask.await()

        val index = jdet.journeyDetail.stops.indexOfFirst { s -> s.id == sourceStopId }
        stops = jdet.journeyDetail.stops.subList(index, jdet.journeyDetail.stops.size)
        var leapTime = 0
        for((index, stop) in stops.withIndex()) {
            if (index > 0) {
                val previous = stops[index-1]
                if (index < stops.size-1) {
                    leapTime += timeDifference(previous.depDate, previous.depTime, stop.depDate, stop.depTime)
                }
                else {
                    leapTime += timeDifference(previous.depDate, previous.depTime, stop.arrDate, stop.arrTime)
                }
                stop.timeDiff = leapTime
            }
        }

        updateFun()
    }
}