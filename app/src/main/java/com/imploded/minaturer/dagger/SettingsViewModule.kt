package com.imploded.minaturer.dagger

import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.interfaces.SettingsViewModelInterface
import com.imploded.minaturer.viewmodel.SettingsViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SettingsViewModule {
    @Provides
    @Singleton
    fun provideSettingsViewModel(settings: SettingsInterface): SettingsViewModelInterface = SettingsViewModel(settings)
}