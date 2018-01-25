package com.imploded.minaturer.model

import com.google.gson.annotations.SerializedName

data class TlDepartureContainer(@SerializedName("Departure")val departures: List<TlDeparture> = listOf())

data class TlDeparture(
        val name: String = "",
        val stop: String = "",
        val stopid: String = "",
        val time: String = "",
        val date: String = "",
        val direction: String = "",
        val transportNumber: String = "",
        val rtTime: String = "",
        val rtDate: String = "",
        val rtTrack: String = "",
        val rtDepTrack: String = "",
        @SerializedName("Product") val product: TlProduct,
        @SerializedName("Stops") val stopContainer: TlStopContainer = TlStopContainer()
)

data class TlProduct(val name: String = "", val num: String = "", val operatorCode: String = "", val catOutCode: Int = 0)

data class TlStopContainer(@SerializedName("Stop")val stops: List<TlStop> = listOf())
data class TlStop(val name: String, val id: String, val depTime: String, val depDate: String, val arrTime: String, val arrDate: String, val routeIdx: Int)