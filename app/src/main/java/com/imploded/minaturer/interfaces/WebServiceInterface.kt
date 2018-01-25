package com.imploded.minaturer.interfaces

import com.imploded.minaturer.model.*

interface WebServiceInterface {
    fun getLocationsByNameTl(expr: String) : LocationContainer
    fun getDeparturesTl(id: String, products: Int) : DepartureContainer
}