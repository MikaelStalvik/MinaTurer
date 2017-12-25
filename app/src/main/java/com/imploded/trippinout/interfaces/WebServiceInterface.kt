package com.imploded.trippinout.interfaces

import com.imploded.trippinout.model.DepartureBoard
import com.imploded.trippinout.model.DepartureContainer
import com.imploded.trippinout.model.LocationContainer
import com.imploded.trippinout.model.Token

interface WebServiceInterface {
    fun getToken(): Token
    fun getLocationsByName(expr: String) : LocationContainer
    fun getDepartures(id: String) : DepartureContainer
}