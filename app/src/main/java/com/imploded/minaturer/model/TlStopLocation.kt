package com.imploded.minaturer.model

import com.google.gson.annotations.SerializedName

data class TlStopLocation(@SerializedName("StopLocation") val stopList: List<TlStopLocationItem> = listOf())

data class TlStopLocationItem(val id: String, val extId: String, val name: String, val lon: Double, val lat: Double, val weight: Int, val products: Int)