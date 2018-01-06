package com.imploded.minaturer.model

import com.google.gson.annotations.SerializedName

data class JourneyDetailsContainer(@SerializedName("JourneyDetail") val journeyDetail: JourneyDetail)

data class JourneyDetail (
        @SerializedName("servertime")val serverTime: String = "",
        @SerializedName("serverdate") val serverDate: String = "",
        @SerializedName("Stop") val stops: List<Stop> = listOf()
)

data class Stop (
    val name: String = "",
    val id: String = "",
    val lon: String = "",
    val lat: String = "",
    val routeIdx: String = "",
    val depTime: String = "",
    val depDate: String = "",
    val rtDepDate: String = "",
    val rtDepTime: String = "",
    val track: String = "",
    val arrTime: String = "",
    val arrDate: String = "",
    var timeDiff: Int = 0
)