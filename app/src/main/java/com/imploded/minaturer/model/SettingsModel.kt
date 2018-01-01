package com.imploded.minaturer.model

data class SettingsModel(
    var StopsList: String,
    var FilteredTripsByStopId: String,
    var FilteredLinesByStopId: String
)

data class FilteredDeparture(
        val shortName: String = "",
        val direction: String = ""
)