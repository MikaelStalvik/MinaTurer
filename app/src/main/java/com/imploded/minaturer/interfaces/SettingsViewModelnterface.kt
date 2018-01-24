package com.imploded.minaturer.interfaces

import com.imploded.minaturer.model.SettingsModel


interface SettingsViewModelInterface {

    fun activeFilters() : Int
    fun saveFilters(
            expressTrain: Boolean,
            regionalTrain: Boolean,
            expressBus: Boolean,
            localTrain: Boolean,
            subway: Boolean,
            tram: Boolean,
            bus: Boolean,
            ferry: Boolean
    )
}
