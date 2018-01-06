package com.imploded.minaturer.model

import com.google.gson.annotations.SerializedName

data class DepartureContainer(@SerializedName("DepartureBoard") val departureBoard: DepartureBoard)

data class DepartureBoard (
        @SerializedName("servertime")val serverTime: String = "",
        @SerializedName("serverdate") val serverDate: String = "",
        @SerializedName("Departure") val departures: List<Departure> = listOf()
)

data class JourneyRef (
    @SerializedName("ref")val ref: String = ""
)

data class Departure (
        val name: String = "",
        val sname: String = "",
        val type: String = "",
        val stop: String = "",
        val stopid: String = "",
        val time: String = "",
        val date: String = "",
        val journeyid: String = "",
        val direction: String = "",
        val track: String = "",
        val rtTime: String? = "",
        val rtDate: String = "",
        val fgColor: String = "",
        val bgColor: String = "",
        val stroke: String = "",
        val accessibility: String = "",
        @SerializedName("JourneyDetailRef")
        val journeyRefIds: JourneyRef
)

data class UiDeparture(
        val name: String = "",
        val shortName: String = "",
        val time: String = "",
        val date: String = "",
        val fgColor: String = "",
        val bgColor: String = "",
        val stop: String = "",
        val rtTime: String? = "",
        val direction: String = "",
        val stopId: String = "",
        var checked: Boolean = false,
        val journeyRefIds: JourneyRef,
        var index : Int = 0
)