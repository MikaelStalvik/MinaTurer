package com.imploded.minaturer.model

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