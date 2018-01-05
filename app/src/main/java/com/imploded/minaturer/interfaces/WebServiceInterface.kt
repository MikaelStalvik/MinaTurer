package com.imploded.minaturer.interfaces

import com.imploded.minaturer.model.DepartureContainer
import com.imploded.minaturer.model.JourneyDetailsContainer
import com.imploded.minaturer.model.LocationContainer
import com.imploded.minaturer.model.Token

interface WebServiceInterface {
    fun getToken(): Token
    fun getLocationsByName(expr: String) : LocationContainer
    fun getDepartures(id: String) : DepartureContainer
    fun getJourneyDetails(ref: String) : JourneyDetailsContainer
}