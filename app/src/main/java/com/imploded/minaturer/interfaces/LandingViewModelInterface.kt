package com.imploded.minaturer.interfaces

import com.imploded.minaturer.model.UiStop

interface LandingViewModelInterface {
    var selectedStops: ArrayList<UiStop>
    fun getStops()
    fun removeStop(position: Int)
}
