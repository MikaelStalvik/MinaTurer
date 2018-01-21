package com.imploded.minaturer.interfaces

import com.imploded.minaturer.model.*

interface WebServiceInterface {
    /*
    fun getToken(): Token
    fun getLocationsByName(expr: String) : LocationContainer
    fun getDepartures(id: String) : DepartureContainer
    fun getJourneyDetails(endpoint: String) : JourneyDetailsContainer
*/
    fun getLocationsByNameTl(expr: String) : LocationContainer
    fun getDeparturesTl(id: String) : DepartureContainer
}