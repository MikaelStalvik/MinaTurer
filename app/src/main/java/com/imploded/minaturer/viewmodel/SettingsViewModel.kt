package com.imploded.minaturer.viewmodel

import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.interfaces.SettingsViewModelInterface
import com.imploded.minaturer.utils.AppConstants
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val settings: SettingsInterface) : SettingsViewModelInterface {
    override fun activeFilters(): Int {
        return settings.loadSettings().activeFilters
    }

    override fun saveFilters(
            expressTrain: Boolean,
            regionalTrain: Boolean,
            expressBus: Boolean,
            localTrain: Boolean,
            subway: Boolean,
            tram: Boolean,
            bus: Boolean,
            ferry: Boolean
    ) {
        var res = 0
        if (expressTrain) res += AppConstants.ExpressTrain
        if (regionalTrain) res += AppConstants.RegionalTrain
        if (expressBus) res += AppConstants.ExpressBus
        if (localTrain) res += AppConstants.LocalTrain
        if (subway) res += AppConstants.Subway
        if (tram) res += AppConstants.Tram
        if (bus) res += AppConstants.Bus
        if (ferry) res += AppConstants.Ferry
        var activeSettings = settings.loadSettings()
        activeSettings.activeFilters = res
        settings.saveSettings(activeSettings)
    }

}