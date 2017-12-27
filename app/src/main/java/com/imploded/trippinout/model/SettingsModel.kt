package com.imploded.trippinout.model

data class SettingsModel(
    var StopsList: String,
    var FilteredTripsByStopId: String
    //var FilteredTripsByDeparture: String
)

data class FilteredDeparture(
        val shortName: String = "",
        val direction: String = ""
)