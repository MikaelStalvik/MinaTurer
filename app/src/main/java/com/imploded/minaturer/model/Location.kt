package com.imploded.minaturer.model

import com.google.gson.annotations.SerializedName

data class LocationContainer(@SerializedName("LocationList") val locationList: LocationList)

data class LocationList(
        @SerializedName("servertime") val serverTime: String = "",
        @SerializedName("serverdate") val serverDate: String = "",
        @SerializedName("StopLocation") val stopLocations: List<StopLocation> = listOf()
)

data class StopLocation(val name: String, val idx: Int, val lon: String, val lat: String, val id: String = "")

data class UiStop(val name: String, val id: String, val lat: String, val lon: String)