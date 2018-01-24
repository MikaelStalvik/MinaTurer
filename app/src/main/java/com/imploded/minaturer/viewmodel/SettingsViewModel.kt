package com.imploded.minaturer.viewmodel

import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.interfaces.SettingsViewModelInterface
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}