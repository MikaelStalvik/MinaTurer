package com.imploded.minaturer.interfaces

import com.imploded.minaturer.model.UiDeparture
import com.imploded.minaturer.model.UiStop

interface OnFragmentInteractionListener {
    fun onFindStopsSelected(data: Int)
    fun onStopSelected(data: UiStop)
    fun onStopAdded(name: String)
    fun onJourneyDetailsSelected(stopId: String, departure: UiDeparture)
    fun sendFirebaseEvent(id: String, itemName: String = "")
}