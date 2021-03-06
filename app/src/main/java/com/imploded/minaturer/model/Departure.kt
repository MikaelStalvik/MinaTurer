package com.imploded.minaturer.model

import com.google.gson.annotations.SerializedName

data class DepartureContainer(@SerializedName("DepartureBoard") val departureBoard: DepartureBoard)

data class DepartureBoard (
        @SerializedName("servertime")val serverTime: String = "",
        @SerializedName("serverdate") val serverDate: String = "",
        @SerializedName("Departure") val departures: List<Departure> = listOf()
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
        val accessibility: String = "",
        val stops: List<Stop> = listOf(),
        val catOutCode: Int
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
        var index : Int = 0,
        val stops: List<Stop> = listOf(),
        val catOutCode: Int
)
