package com.imploded.minaturer.viewmodel

import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.model.Stop
import com.imploded.minaturer.repository.WebServiceRepository
import com.imploded.minaturer.utils.timeDifference
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

interface JourneyDetailViewModelInterface {
    var stops: List<Stop>
    fun setInputParameters(sourceRef: String, sourceStopId: String)
    fun getJourneyDetails(updateFun: (() -> Unit)) : Deferred<Unit>
}

//class JourneyDetailViewModel(private val sourceRef: String, private val sourceStopId: String) : JourneyDetailViewModelInterface {
class JourneyDetailViewModel(private val webservice: WebServiceInterface) : JourneyDetailViewModelInterface {

    //private val webservice: WebServiceInterface = WebServiceRepository()

    private lateinit var sourceRef: String
    private lateinit var sourceStopId: String
    override fun setInputParameters(sourceRef: String, sourceStopId: String) {
        this.sourceRef = sourceRef
        this.sourceStopId = sourceStopId
    }

    override var stops: List<Stop> = listOf()

    override fun getJourneyDetails(updateFun: (() -> Unit)) = async(UI) {
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

        updateFun()
    }
}