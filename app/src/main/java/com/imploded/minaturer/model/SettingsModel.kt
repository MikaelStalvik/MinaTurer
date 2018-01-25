package com.imploded.minaturer.model

data class SettingsModel(
    var StopsList: String,
    var FilteredTripsByStopId: String,
    var FilteredLinesByStopId: String,
    var LandingHintPageShown: Boolean,
    var FindStopHintShown: Boolean,
    var DeparturesHintShown: Boolean,
    var IsRated: Boolean,
    var StartCount: Int,
    var activeFilters: Int
)

data class FilteredDeparture(
        val shortName: String = "",
        val direction: String = ""
)