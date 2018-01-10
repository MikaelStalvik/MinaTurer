package com.imploded.minaturer.dagger

import com.imploded.minaturer.interfaces.LandingViewModelInterface
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.viewmodel.LandingViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LandingViewModule {
    @Provides
    @Singleton
    fun provideLandingViewModel(settings: SettingsInterface): LandingViewModelInterface = LandingViewModel(settings)
}