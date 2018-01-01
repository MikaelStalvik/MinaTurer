package com.imploded.minaturer.interfaces

import com.imploded.minaturer.model.UiStop

interface OnFragmentInteractionListener {
    fun onFindStopsSelected(data: Int)
    fun onStopSelected(data: UiStop)
    fun onStopAdded(name: String)
}